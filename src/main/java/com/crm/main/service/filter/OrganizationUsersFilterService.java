package com.crm.main.service.filter;

import com.crm.main.dto.request.UserAndRolesFilterRequest;
import com.crm.main.mapper.OrganizationUserMapper;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.wrapper.UserClientWrapper;
import com.crm.sharedlib.dto.request.UserFilterRequest;
import com.crm.sharedlib.dto.request.UserWithRolesFilterRequest;
import com.crm.sharedlib.dto.response.UserAndRoles;
import com.crm.sharedlib.dto.response.UserWithRoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class OrganizationUsersFilterService {

    private final OrganizationUserService organizationUserService;

    private final OrganizationRoleUserService roleUserService;

    private final UserClientWrapper userClientWrapper;

    private final OrganizationUserMapper organizationUserMapper;

    @Transactional
    public PagedModel<UserWithRoleResponse> filterOrganizationUsers(
            Organization organization,
            UserAndRolesFilterRequest request
    ) {

        Page<OrganizationUser> usersInOrganization;

        if (nonNull(request.getRolesId()) && !request.getRolesId().isEmpty()) {
            usersInOrganization = applyRolesFilter(request, organization);
        } else {
            usersInOrganization = filterOrganizationUsers(request, organization);
        }

        Page<UserWithRoleResponse> result = getFilteredUsersFromAuthService(request, usersInOrganization);

        return new PagedModel<>(result);

    }

    private Page<OrganizationUser> applyRolesFilter(
            UserAndRolesFilterRequest request, Organization organization
    ) {
        return roleUserService.filterOrganizationRolesByIds(
                request.getRolesId(),
                organization,
                request.getPage(),
                request.getSize()
        ).map(OrganizationRoleUser::getOrganizationUser);
    }

    private Page<OrganizationUser> filterOrganizationUsers(
            UserAndRolesFilterRequest request, Organization organization
    ) {
        return organizationUserService.getUsersInOrganization(
                organization,
                request.getPage(),
                request.getSize()
        );
    }

    private Page<UserWithRoleResponse> getFilteredUsersFromAuthService(
            UserFilterRequest request, Page<OrganizationUser> usersInOrganization
    ) {
        List<UserAndRoles> userAndRoles = usersInOrganization.getContent()
                .stream()
                .map(organizationUserMapper::toUserAndRoleResponse)
                .toList();

        UserWithRolesFilterRequest rolesFilterRequest = organizationUserMapper.toRolesFilterRequest(request);

        rolesFilterRequest.setUserAndRoles(userAndRoles);

        rolesFilterRequest.setPage(0);

        List<UserWithRoleResponse> userWithRoleResponse = userClientWrapper.filterUsers(rolesFilterRequest).getContent();

        return new PageImpl<>(
                userWithRoleResponse,
                usersInOrganization.getPageable(),
                usersInOrganization.getTotalElements()
        );
    }

}
