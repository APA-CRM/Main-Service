package com.crm.main.controller;

import com.crm.main.annotations.OrganizationId;
import com.crm.main.annotations.RequiresOrganizationMembership;
import com.crm.main.annotations.UserId;
import com.crm.main.dto.request.UserAndRolesFilterRequest;
import com.crm.main.facade.OrganizationUsersFacade;
import com.crm.sharedlib.core.dto.response.UserWithRoleResponse;
import com.crm.sharedlib.core.enums.Action;
import com.crm.sharedlib.core.enums.Resource;
import com.crm.sharedlib.rbac.annotation.RequiresPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationUsersController {

    private final OrganizationUsersFacade facade;

    @GetMapping("/{organizationId}/users/filter")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.USERS, action = Action.READ)
    public PagedModel<UserWithRoleResponse> filterOrganizationUsers(
            @Valid @ModelAttribute UserAndRolesFilterRequest request,
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        return facade.filterOrganizationUsers(
                request, organizationId
        );
    }

    @DeleteMapping("/{organizationId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.USERS, action = Action.DELETE)
    public void removeUserFromOrganization(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @PathVariable("userId") Long userIdToAddToOrganization,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        facade.removeUserFromOrganization(
                organizationId,
                userIdToAddToOrganization
        );
    }

}
