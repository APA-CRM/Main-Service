package com.crm.main.service.assignments;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.service.OrganizationRoleService;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.producer.OrgUserRoleChangedProducer;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.sharedlib.dto.request.RoleRequest;
import com.crm.sharedlib.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationRoleAssignmentService {

    private final OrganizationRoleService roleService;

    private final OrganizationRoleUserService roleUserService;

    private final RoleClientWrapper roleClientWrapper;

    private final OrgUserRoleChangedProducer producer;

    @Transactional
    public RoleResponse createOrganizationRole(Organization organization, RoleRequest request) {
        RoleResponse role = roleClientWrapper.createRole(request);

        roleService.createRoleForOrganization(organization, role.getId());

        return role;
    }

    public RoleResponse updateOrganizationRole(
            RoleRequest request, Organization organization, Long roleId
    ) {
        OrganizationRole role = roleService.getOrganizationRole(organization, roleId);

        List<OrganizationRoleUser> userRoles = roleUserService.getOrganizationUserRoles(role);

        RoleResponse roleResponse = roleClientWrapper.updateRole(request, roleId);

        // TODO: Think how to improve this code. It's cause N+1 JPA problem
        for (OrganizationRoleUser organizationRoleUser : userRoles) {
            producer.sendOrgUserRoleChanged(organization, organizationRoleUser.getOrganizationUser());
        }

        return roleResponse;
    }

    @Transactional
    public void deleteOrganizationRole(Organization organization, Long roleId) {

        OrganizationRole role = roleService.getOrganizationRole(organization, roleId);

        List<OrganizationRoleUser> userRoles = roleUserService.getOrganizationUserRoles(role);

        roleUserService.deleteRole(role);

        roleService.deleteOrganizationRole(role);

        roleClientWrapper.deleteRole(roleId);

        // TODO: Think how to improve this code. It's cause N+1 JPA problem
        for (OrganizationRoleUser organizationRoleUser : userRoles) {
            producer.sendOrgUserRoleChanged(organization, organizationRoleUser.getOrganizationUser());
        }

    }

}
