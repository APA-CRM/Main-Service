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

import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationUsersController {

    private final OrganizationUsersFacade facade;

    @PostMapping("/{organizationId}/users")
    public PagedModel<UserWithRoleResponse> getUsersOfOrganization(
            @Valid @RequestBody UserAndRolesFilterRequest request,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @PathVariable("organizationId") Long organizationId
    ) {
        return facade.filterOrganizationUsers(
                request, userId, organizationId
        );
    }

    @PutMapping("/{organizationId}/users/{userId}")
    public UserResponse addUserToOrganization(
            @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @PathVariable("userId") Long userIdToAddToOrganization
    ) {
        return facade.addUserToOrganization(
                userId, organizationId,
                userIdToAddToOrganization
        );
    }

    @DeleteMapping("/{organizationId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromOrganization(
            @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @PathVariable("userId") Long userIdToAddToOrganization
    ) {
        facade.removeUserFromOrganization(
                userId, organizationId,
                userIdToAddToOrganization
        );

        return ResponseEntity.noContent().build();
    }

}
