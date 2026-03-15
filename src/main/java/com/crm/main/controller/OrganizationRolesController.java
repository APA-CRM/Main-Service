package com.crm.main.controller;

import com.crm.main.annotations.OrganizationId;
import com.crm.main.annotations.RequiresOrganizationMembership;
import com.crm.main.annotations.UserId;
import com.crm.main.facade.OrganizationRolesFacade;
import com.crm.sharedlib.core.dto.request.RoleFilterRequest;
import com.crm.sharedlib.core.dto.request.RoleRequest;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import com.crm.sharedlib.core.enums.Action;
import com.crm.sharedlib.core.enums.Resource;
import com.crm.sharedlib.rbac.annotation.RequiresPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;

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
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.ROLES, action = Action.READ)
    public List<RoleResponse> getOrganizationRoles(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        return facade.getOrganizationRoles(organizationId);
    }

    @PostMapping("/{organizationId}/roles")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.ROLES, action = Action.CREATE)
    public RoleResponse createRole(
            @Valid
            @RequestBody
            RoleRequest request,
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        return facade.createRole(request, organizationId);
    }

    @GetMapping("/{organizationId}/roles/filter")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.ROLES, action = Action.READ)
    public PagedModel<RoleResponse> filterOrganizationRoles(
            @Valid
            @ModelAttribute
            RoleFilterRequest request,
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        return facade.filterOrganizationRoles(request, organizationId);
    }

    @PutMapping("/{organizationId}/roles/{roleId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.ROLES, action = Action.UPDATE)
    public RoleResponse updateOrganizationRole(
            @Valid
            @RequestBody
            RoleRequest request,
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        return facade.updateOrganizationRole(request, organizationId, roleId);
    }

    @DeleteMapping("/{organizationId}/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.ROLES, action = Action.DELETE)
    public void deleteOrganizationRole(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @PathVariable("roleId") Long roleId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        facade.deleteOrganizationRole(organizationId, roleId);
    }
}
