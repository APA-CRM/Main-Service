package com.crm.main.controller;

import com.crm.main.annotations.OrganizationId;
import com.crm.main.annotations.RequiresOrganizationMembership;
import com.crm.main.annotations.UserId;
import com.crm.main.facade.OrganizationUserRoleFacade;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import com.crm.sharedlib.core.enums.Action;
import com.crm.sharedlib.core.enums.Resource;
import com.crm.sharedlib.rbac.annotation.RequiresPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationUserRoleController {

    private final OrganizationUserRoleFacade facade;

    @PutMapping("/{organizationId}/users/{userId}/roles/{roleId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.USERS_ROLES, action = Action.CREATE)
    public RoleResponse addRoleForUser(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @PathVariable("userId") Long userId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long authorizedUser
    ) {
        return facade.addRoleForUserInOrganization(organizationId, roleId, userId, authorizedUser);
    }

    @DeleteMapping("/{organizationId}/users/{userId}/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.USERS_ROLES, action = Action.DELETE)
    public void removeRoleForUserOrganization(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @PathVariable("userId") Long userId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long authorizedUser
    ) {
        facade.removeRoleForUserOrganization(organizationId, roleId, userId, authorizedUser);
    }
}
