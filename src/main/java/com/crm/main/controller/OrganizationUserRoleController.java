package com.crm.main.controller;

import com.crm.main.facade.OrganizationUserRoleFacade;
import com.crm.sharedlib.core.dto.response.RoleResponse;
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
    public RoleResponse addRoleForUser(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @PathVariable("userId") Long userId,
            @RequestHeader(USER_ID_HEADER_NAME) Long authorizedUser
    ) {
        return facade.addRoleForUserInOrganization(organizationId, roleId, userId, authorizedUser);
    }

    @DeleteMapping("/{organizationId}/users/{userId}/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRoleForUserOrganization(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @PathVariable("userId") Long userId,
            @RequestHeader(USER_ID_HEADER_NAME) Long authorizedUser
    ) {
        facade.removeRoleForUserOrganization(organizationId, roleId, userId, authorizedUser);
    }
}
