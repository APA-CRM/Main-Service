package com.crm.main.controller;

import com.crm.main.facade.OrganizationUserRoleFacade;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationUserRoleController {

    private final OrganizationUserRoleFacade facade;

    @PutMapping("/{organizationId}/users/{userId}/roles/{roleId}")
    public RoleResponse addRoleForUser(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @PathVariable("userId") Long userId
    ) {
        return facade.addRoleForUserInOrganization(organizationId, roleId, userId);
    }

    @DeleteMapping("/{organizationId}/users/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleForUserOrganization(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @PathVariable("userId") Long userId
    ) {
        facade.removeRoleForUserOrganization(organizationId, roleId, userId);

        return ResponseEntity.noContent().build();
    }
}
