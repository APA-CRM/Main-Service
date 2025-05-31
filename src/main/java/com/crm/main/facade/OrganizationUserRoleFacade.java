package com.crm.main.facade;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationRoleService;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.sharedlib.annotations.Facade;
import com.crm.sharedlib.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class OrganizationUserRoleFacade {

    private final OrganizationService organizationService;

    private final OrganizationRoleService roleService;

    private final OrganizationUserService userService;

    private final OrganizationRoleUserService roleUserService;

    private final RoleClientWrapper roleClientWrapper;

    @Transactional
    public RoleResponse addRoleForUserInOrganization(
            Long organizationId, Long roleId,
            Long userId, Long authUserId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        userService.getIsUserInOrganizationOrThrowException(organization, authUserId);

        OrganizationRole role = roleService.getOrganizationRole(organization, roleId);

        OrganizationUser organizationUser = userService.getOrganizationUser(organization, userId);

        roleUserService.createRoleForOrganizationUser(role, organizationUser);

        return roleClientWrapper.getRole(roleId);
    }

    @Transactional
    public void removeRoleForUserOrganization(
            Long organizationId, Long roleId,
            Long userId, Long authUserId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        userService.getIsUserInOrganizationOrThrowException(organization, authUserId);

        OrganizationRole role = roleService.getOrganizationRole(organization, roleId);

        OrganizationUser organizationUser = userService.getOrganizationUser(organization, userId);

        OrganizationRoleUser roleOfUserOrganization =
                roleUserService.getRoleOfUserOrganization(role, organizationUser);

        roleUserService.deleteRoleForUser(roleOfUserOrganization);
    }

}
