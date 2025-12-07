package com.crm.main.facade.internal;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.OrganizationUserService;
import com.crm.sharedlib.core.annotations.Facade;
import com.crm.sharedlib.core.dto.response.OrganizationUserRolesResponse;
import com.crm.sharedlib.core.dto.response.UserExistsInOrganizationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class InternalOrganizationUserFacade {

    private final OrganizationService organizationService;
    private final OrganizationUserService userService;
    private final OrganizationRoleUserService roleUserService;

    @Transactional
    public OrganizationUserRolesResponse getOrganizationUserRoles(
            Long organizationId, Long userId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        OrganizationUser organizationUser =
                userService.getOrganizationUser(organization, userId);

        List<OrganizationRoleUser> userRoles =
                roleUserService.getOrganizationUserRoles(organizationUser);

        List<Long> rolesId = userRoles.stream()
                .map(role -> role.getOrganizationRole().getRoleId())
                .toList();

        return OrganizationUserRolesResponse.builder()
                .organizationId(organizationId)
                .userId(userId)
                .rolesId(rolesId)
                .build();
    }

    public UserExistsInOrganizationResponse isUserExistsInOrganization(
            Long organizationId, Long userId
    ) {
        boolean userExistsInOrganization = userService.isUserExistsInOrganization(organizationId, userId);

        return new UserExistsInOrganizationResponse(userExistsInOrganization);
    }

}
