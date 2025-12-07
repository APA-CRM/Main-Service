package com.crm.main.feign;

import com.crm.sharedlib.core.dto.request.CreateRoleRequest;
import com.crm.sharedlib.core.dto.request.RoleFilterRequest;
import com.crm.sharedlib.core.dto.request.RoleRequest;
import com.crm.sharedlib.core.dto.request.UserFilterRequest;
import com.crm.sharedlib.core.dto.response.RestResponsePage;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import com.crm.sharedlib.core.dto.response.UserResponse;
import com.crm.sharedlib.core.dto.response.UserWithRoleResponse;
import com.crm.sharedlib.core.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "auth-service", configuration = FeignClientConfig.class)
public interface AuthClient {

    @PostMapping("/api/internal/roles")
    RoleResponse createRole(@RequestBody CreateRoleRequest request);

    @PutMapping("/api/internal/roles/{roleId}")
    RoleResponse updateRole(@RequestBody RoleRequest request, @PathVariable("roleId") Long roleId);

    @GetMapping("/api/internal/roles")
    List<RoleResponse> getRole(@RequestParam("roleId") List<Long> rolesId);

    @PostMapping("/api/internal/roles/filter")
    RestResponsePage<RoleResponse> filterRoles(@RequestBody RoleFilterRequest request);

    @DeleteMapping("/api/internal/roles/{roleId}")
    void deleteRole(@PathVariable("roleId") Long roleId);

    @PostMapping("/api/internal/users/filter")
    RestResponsePage<UserWithRoleResponse> filterUsers(@RequestBody UserFilterRequest request);

    @GetMapping("/api/internal/users/{userId}")
    UserResponse getUserById(@PathVariable("userId") Long userId);

}
