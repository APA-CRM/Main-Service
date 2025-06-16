package com.crm.main.facade;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.service.OrganizationRoleService;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.assignments.OrganizationRoleAssignmentService;
import com.crm.main.service.filter.OrganizationRoleFilterService;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.sharedlib.annotations.Facade;
import com.crm.sharedlib.dto.request.RoleFilterRequest;
import com.crm.sharedlib.dto.request.RoleRequest;
import com.crm.sharedlib.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class OrganizationRolesFacade {

    private final OrganizationService organizationService;

    private final OrganizationRoleService roleService;

    private final RoleClientWrapper roleClientWrapper;

    private final OrganizationRoleFilterService filterService;

    private final OrganizationRoleAssignmentService assignmentService;

    @Transactional
    public List<RoleResponse> getOrganizationRoles(Long organizationId) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        List<Long> rolesId = roleService.getOrganizationsRoles(organization)
                .stream()
                .map(OrganizationRole::getRoleId)
                .toList();

        return roleClientWrapper.getRolesByIds(rolesId);
    }

    @Transactional
    public RoleResponse createRole(
            RoleRequest request,
            Long organizationId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        return assignmentService.createOrganizationRole(organization, request);
    }

    public PagedModel<RoleResponse> filterOrganizationRoles(
            RoleFilterRequest request,
            Long organizationId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        return filterService.filterRoles(request, organization);
    }

    public RoleResponse updateOrganizationRole(
            RoleRequest request, Long organizationId,
            Long roleId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        return assignmentService.updateOrganizationRole(request, organization, roleId);
    }

    @Transactional
    public void deleteOrganizationRole(
            Long organizationId, Long roleId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        assignmentService.deleteOrganizationRole(organization, roleId);
    }

}
