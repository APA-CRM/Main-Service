package com.crm.main.controller;

import com.crm.main.facade.OrganizationRolesFacade;
import com.crm.sharedlib.dto.request.RoleFilterRequest;
import com.crm.sharedlib.dto.request.RoleRequest;
import com.crm.sharedlib.dto.response.RoleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationRolesController {

    private final OrganizationRolesFacade facade;

    /**
     * @deprecated Use filter endpoint {@link #filterOrganizationRoles(RoleFilterRequest, Long, Long)}
     */
    @Deprecated(forRemoval = true)
    @GetMapping("/{organizationId}/roles")
    public List<RoleResponse> getOrganizationRoles(
            @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId
    ) {
        return facade.getOrganizationRoles(organizationId, userId);
    }

    @PostMapping("/{organizationId}/roles")
    public RoleResponse createRole(
            @Valid
            @RequestBody
            RoleRequest request,
            @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId
    ) {
        return facade.createRole(request, organizationId, userId);
    }

    @PostMapping("/{organizationId}/roles/filter")
    public PagedModel<RoleResponse> filterOrganizationRoles(
            @Valid
            @RequestBody
            RoleFilterRequest request,
            @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId
    ) {
        return facade.filterOrganizationRoles(request, organizationId, userId);
    }

    @PutMapping("/{organizationId}/roles/{roleId}")
    public RoleResponse updateOrganizationRole(
            @Valid
            @RequestBody
            RoleRequest request,
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId
    ) {
        return facade.updateOrganizationRole(request, organizationId, roleId, userId);
    }

    @DeleteMapping("/{organizationId}/roles/{roleId}")
    public ResponseEntity<Void> deleteOrganizationRole(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId
    ) {
        facade.deleteOrganizationRole(organizationId, roleId, userId);

        return ResponseEntity.noContent().build();
    }
}
