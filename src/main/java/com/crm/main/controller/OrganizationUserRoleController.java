package com.crm.main.controller;

import com.crm.main.facade.OrganizationUserRoleFacade;
import com.crm.sharedlib.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;

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
            @RequestHeader(USER_ID_HEADER_NAME) Long authUserId
    ) {
        return facade.addRoleForUserInOrganization(organizationId, roleId, userId, authUserId);
    }

    @DeleteMapping("/{organizationId}/users/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleForUserOrganization(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @PathVariable("userId") Long userId,
            @RequestHeader(USER_ID_HEADER_NAME) Long authUserId
    ) {
        facade.removeRoleForUserOrganization(organizationId, roleId, userId, authUserId);

        return ResponseEntity.noContent().build();
    }
}
