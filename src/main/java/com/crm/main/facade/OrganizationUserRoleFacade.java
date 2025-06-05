package com.crm.main.facade;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.assignments.OrganizationUserRoleAssignmentService;
import com.crm.sharedlib.annotations.Facade;
import com.crm.sharedlib.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class OrganizationUserRoleFacade {

    private final OrganizationService organizationService;
    private final OrganizationUserService userService;

    private final OrganizationUserRoleAssignmentService assignmentService;

    public RoleResponse addRoleForUserInOrganization(
            Long organizationId, Long roleId,
            Long userId, Long authUserId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        userService.getIsUserInOrganizationOrThrowException(organization, authUserId);

        return assignmentService.addRoleForUserInOrganization(organization, roleId, userId);
    }

    public void removeRoleForUserOrganization(
            Long organizationId, Long roleId,
            Long userId, Long authUserId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        userService.getIsUserInOrganizationOrThrowException(organization, authUserId);

        assignmentService.removeRoleForUserOrganization(organization, roleId, userId);
    }

}
