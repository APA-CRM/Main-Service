package com.crm.main.controller;

import com.crm.main.dto.request.UserAndRolesFilterRequest;
import com.crm.main.facade.OrganizationUsersFacade;
import com.crm.sharedlib.dto.response.UserResponse;
import com.crm.sharedlib.dto.response.UserWithRoleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationUsersController {

    private final OrganizationUsersFacade facade;

    @PostMapping("/{organizationId}/users/filter")
    public PagedModel<UserWithRoleResponse> filterOrganizationUsers(
            @Valid @RequestBody UserAndRolesFilterRequest request,
            @PathVariable("organizationId") Long organizationId
    ) {
        return facade.filterOrganizationUsers(
                request, organizationId
        );
    }

    @PutMapping("/{organizationId}/users/{userId}")
    public UserResponse addUserToOrganization(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("userId") Long userIdToAddToOrganization
    ) {
        return facade.addUserToOrganization(
                organizationId,
                userIdToAddToOrganization
        );
    }

    @DeleteMapping("/{organizationId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromOrganization(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("userId") Long userIdToAddToOrganization
    ) {
        facade.removeUserFromOrganization(
                organizationId,
                userIdToAddToOrganization
        );

        return ResponseEntity.noContent().build();
    }

}
