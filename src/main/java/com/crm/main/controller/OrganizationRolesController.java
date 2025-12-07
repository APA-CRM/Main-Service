package com.crm.main.controller;

import com.crm.main.facade.OrganizationRolesFacade;
import com.crm.sharedlib.core.dto.request.RoleFilterRequest;
import com.crm.sharedlib.core.dto.request.RoleRequest;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationRolesController {

    private final OrganizationRolesFacade facade;

    /**
     * @deprecated Use filter endpoint {@link #filterOrganizationRoles(RoleFilterRequest, Long)}
     */
    @Deprecated(forRemoval = true)
    @GetMapping("/{organizationId}/roles")
    public List<RoleResponse> getOrganizationRoles(
            @PathVariable("organizationId") Long organizationId
    ) {
        return facade.getOrganizationRoles(organizationId);
    }

    @PostMapping("/{organizationId}/roles")
    public RoleResponse createRole(
            @Valid
            @RequestBody
            RoleRequest request,
            @PathVariable("organizationId") Long organizationId
    ) {
        return facade.createRole(request, organizationId);
    }

    @GetMapping("/{organizationId}/roles/filter")
    public PagedModel<RoleResponse> filterOrganizationRoles(
            @Valid
            @ModelAttribute
            RoleFilterRequest request,
            @PathVariable("organizationId") Long organizationId
    ) {
        return facade.filterOrganizationRoles(request, organizationId);
    }

    @PutMapping("/{organizationId}/roles/{roleId}")
    public RoleResponse updateOrganizationRole(
            @Valid
            @RequestBody
            RoleRequest request,
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId
    ) {
        return facade.updateOrganizationRole(request, organizationId, roleId);
    }

    @DeleteMapping("/{organizationId}/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganizationRole(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId
    ) {
        facade.deleteOrganizationRole(organizationId, roleId);
    }
}
