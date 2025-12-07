package com.crm.main.facade;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.assignments.OrganizationUserRoleAssignmentService;
import com.crm.sharedlib.core.annotations.Facade;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class OrganizationUserRoleFacade {

    private final OrganizationService organizationService;

    private final OrganizationUserRoleAssignmentService assignmentService;

    public RoleResponse addRoleForUserInOrganization(
            Long organizationId, Long roleId,
            Long userId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        return assignmentService.addRoleForUserInOrganization(organization, roleId, userId);
    }

    public void removeRoleForUserOrganization(
            Long organizationId, Long roleId,
            Long userId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        assignmentService.removeRoleForUserOrganization(organization, roleId, userId);
    }

}
