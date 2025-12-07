package com.crm.main.facade;

import com.crm.main.dto.request.UserAndRolesFilterRequest;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.assignments.OrganizationUserAssignmentService;
import com.crm.main.service.filter.OrganizationUsersFilterService;
import com.crm.sharedlib.core.annotations.Facade;
import com.crm.sharedlib.core.dto.response.UserWithRoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;

@Facade
@RequiredArgsConstructor
public class OrganizationUsersFacade {

    private final OrganizationUsersFilterService filterService;

    private final OrganizationService organizationService;

    private final OrganizationUserAssignmentService assignmentService;

    public PagedModel<UserWithRoleResponse> filterOrganizationUsers(
            UserAndRolesFilterRequest request, Long organizationId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        return filterService.filterOrganizationUsers(organization, request);
    }

    public void removeUserFromOrganization(
            Long organizationId,
            Long deletedUserId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        assignmentService.removeUserFromOrganization(organization, deletedUserId);
    }

}
