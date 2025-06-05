package com.crm.main.controller.internal;

import com.crm.main.facade.internal.InternalOrganizationUserRoleFacade;
import com.crm.sharedlib.dto.response.OrganizationUserRolesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/organizations/")
@RequiredArgsConstructor
public class InternalOrganizationUserRoleController {

    private final InternalOrganizationUserRoleFacade facade;

    @GetMapping("/{organizationId}/users/{userId}")
    public OrganizationUserRolesResponse getOrganizationUserRoles(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("userId") Long userId
    ) {
        return facade.getOrganizationUserRoles(organizationId, userId);
    }

}
