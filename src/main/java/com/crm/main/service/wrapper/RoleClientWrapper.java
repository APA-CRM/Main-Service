package com.crm.main.service.wrapper;

import com.crm.main.feign.AuthClient;
import com.crm.sharedlib.dto.request.ResourceWithActionsRequest;
import com.crm.sharedlib.dto.request.RoleFilterRequest;
import com.crm.sharedlib.dto.request.RoleRequest;
import com.crm.sharedlib.dto.response.RoleResponse;
import com.crm.sharedlib.enums.Action;
import com.crm.sharedlib.enums.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleClientWrapper {

    private final AuthClient authClient;

    @Value("${app.roles.default-admin-name}")
    private String adminRoleName;

    // TODO: Move this method to another service class
    public RoleResponse createAdminRoleForOrganization() {
        RoleRequest request = new RoleRequest();

        request.setName(adminRoleName);

        ResourceWithActionsRequest resource = new ResourceWithActionsRequest();
        resource.setResource(Resource.ALL);
        resource.setActions(Collections.singletonList(Action.ALL));

        request.setResources(Collections.singletonList(resource));

        return authClient.createRole(request);
    }

    public RoleResponse createRole(RoleRequest request) {
        return authClient.createRole(request);
    }

    public RoleResponse updateRole(RoleRequest request, Long roleId) {
        return authClient.updateRole(request, roleId);
    }

    public void deleteRole(Long roleId) {
        authClient.deleteRole(roleId);
    }

    public List<RoleResponse> getRolesByIds(List<Long> rolesId) {
        return authClient.getRole(rolesId);
    }

    public RoleResponse getRole(Long roleId) {
        return authClient.getRole(Collections.singletonList(roleId))
                .getFirst();
    }

    public PagedModel<RoleResponse> filterRoles(RoleFilterRequest request) {
        return authClient.filterRoles(request);
    }
}
