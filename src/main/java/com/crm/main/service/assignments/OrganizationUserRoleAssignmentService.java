package com.crm.main.service.assignments;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationRoleService;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.producer.OrgUserRoleChangedProducer;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.sharedlib.dto.response.RoleResponse;
import com.crm.sharedlib.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationUserRoleAssignmentService {

    private final OrganizationUserService userService;
    private final OrganizationRoleService roleService;
    private final OrganizationRoleUserService roleUserService;

    private final RoleClientWrapper roleClientWrapper;

    private final OrgUserRoleChangedProducer roleChangedProducer;

    @Transactional
    public RoleResponse addRoleForUserInOrganization(
            Organization organization, Long roleId, Long userId
    ) {

        OrganizationRole role = roleService.getOrganizationRole(organization, roleId);

        OrganizationUser organizationUser = userService.getOrganizationUser(organization, userId);

        roleUserService.createRoleForOrganizationUser(role, organizationUser);

        RoleResponse roleResponse = roleClientWrapper.getRole(roleId);

        roleChangedProducer.sendOrgUserRoleChanged(organization, organizationUser);

        return roleResponse;
    }

    @Transactional
    public void removeRoleForUserOrganization(
            Organization organization, Long roleId, Long userId
    ) {
        OrganizationRole role = roleService.getOrganizationRole(organization, roleId);

        OrganizationUser organizationUser = userService.getOrganizationUser(organization, userId);

        OrganizationRoleUser roleOfUserOrganization =
                roleUserService.getRoleOfUserOrganization(role, organizationUser);

        if (organizationUser.getOrganizationRoleUsers().size() == 1) {
            throw new ForbiddenException("Role can't be unassigned — user has no other roles");
        }

        roleUserService.deleteRoleForUser(roleOfUserOrganization);

        roleChangedProducer.sendOrgUserRoleChanged(organization, organizationUser);
    }

}
