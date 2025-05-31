package com.crm.main.service.assignments;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.service.OrganizationRoleService;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.sharedlib.dto.request.RoleRequest;
import com.crm.sharedlib.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationRoleAssignmentService {

    private final OrganizationRoleService roleService;

    private final OrganizationRoleUserService roleUserService;

    private final RoleClientWrapper roleClientWrapper;

    @Transactional
    public RoleResponse createOrganizationRole(Organization organization, RoleRequest request) {
        RoleResponse role = roleClientWrapper.createRole(request);

        roleService.createRoleForOrganization(organization, role.getId());

        return role;
    }

    @Transactional
    public void deleteOrganizationRole(Organization organization, Long roleId) {

        OrganizationRole role = roleService.getOrganizationRole(organization, roleId);

        roleUserService.deleteRole(role);

        roleService.deleteOrganizationRole(role);

        roleClientWrapper.deleteRole(roleId);

    }

}
