package com.crm.main.facade;

import com.crm.main.dto.request.UserAndRolesFilterRequest;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.assignments.OrganizationUserAssignmentService;
import com.crm.main.service.filter.OrganizationUsersFilterService;
import com.crm.sharedlib.annotations.Facade;
import com.crm.sharedlib.dto.response.UserResponse;
import com.crm.sharedlib.dto.response.UserWithRoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;

@Facade
@RequiredArgsConstructor
public class OrganizationUsersFacade {

    private final OrganizationUsersFilterService filterService;

    private final OrganizationService organizationService;

    private final OrganizationUserService organizationUserService;

    private final OrganizationUserAssignmentService assignmentService;

    public PagedModel<UserWithRoleResponse> filterOrganizationUsers(
            UserAndRolesFilterRequest request, Long userId, Long organizationId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        organizationUserService.getIsUserInOrganizationOrThrowException(organization, userId);

        return filterService.filterOrganizationUsers(organization, request);
    }

    public UserResponse addUserToOrganization(
            Long userId,
            Long organizationId,
            Long userIdToAddToOrganization
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        organizationUserService.getIsUserInOrganizationOrThrowException(organization, userId);

        return assignmentService.addUserToOrganization(organization, userIdToAddToOrganization);
    }

    public void removeUserFromOrganization(
            Long userId,
            Long organizationId,
            Long deletedUserId
    ) {

        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        organizationUserService.getIsUserInOrganizationOrThrowException(organization, userId);

        assignmentService.removeUserFromOrganization(organization, deletedUserId);
    }

}
