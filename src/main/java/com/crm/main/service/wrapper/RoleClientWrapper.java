package com.crm.main.service.wrapper;

import com.crm.main.feign.AuthClient;
import com.crm.sharedlib.core.dto.request.CreateRoleRequest;
import com.crm.sharedlib.core.dto.request.RoleFilterRequest;
import com.crm.sharedlib.core.dto.request.RoleRequest;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleClientWrapper {

    private final AuthClient authClient;

    public RoleResponse createRole(RoleRequest request, boolean isDeletable) {
        CreateRoleRequest roleRequest = new CreateRoleRequest();
        roleRequest.setIsDeletable(isDeletable);
        roleRequest.setName(request.getName());
        roleRequest.setResources(request.getResources());

        return authClient.createRole(roleRequest);
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
