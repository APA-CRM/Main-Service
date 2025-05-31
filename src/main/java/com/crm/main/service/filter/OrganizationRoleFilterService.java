package com.crm.main.service.filter;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.service.OrganizationRoleService;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.sharedlib.dto.request.RoleFilterRequest;
import com.crm.sharedlib.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationRoleFilterService {

    private final RoleClientWrapper roleClientWrapper;

    private final OrganizationRoleService roleService;

    public PagedModel<RoleResponse> filterRoles(
            RoleFilterRequest request,
            Organization organization
    ) {
        List<OrganizationRole> roles =
                roleService.getOrganizationsRoles(organization);

        return getRolesFromAuthService(request, roles);
    }

    private PagedModel<RoleResponse> getRolesFromAuthService(
            RoleFilterRequest request,
            List<OrganizationRole> roles
    ) {

        List<Long> rolesId = roles.stream()
                .map(OrganizationRole::getRoleId)
                .toList();

        request.setRolesId(rolesId);

        return roleClientWrapper.filterRoles(request);
    }

}
