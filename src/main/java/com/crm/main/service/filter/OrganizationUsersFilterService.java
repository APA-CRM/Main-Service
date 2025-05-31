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

        List<OrganizationUser> usersInOrganization;

        if (nonNull(request.getRolesId()) && !request.getRolesId().isEmpty()) {
            usersInOrganization = applyRolesFilter(request, organization);
        } else {
            usersInOrganization = filterOrganizationUsers(organization);
        }

        return getFilteredUsersFromAuthService(request, usersInOrganization);

    }

    private List<OrganizationUser> applyRolesFilter(
            UserAndRolesFilterRequest request, Organization organization
    ) {
        return roleUserService.filterOrganizationRolesByIds(
                        request.getRolesId(), organization
                ).stream()
                .map(OrganizationRoleUser::getOrganizationUser).toList();
    }

    private List<OrganizationUser> filterOrganizationUsers(
            Organization organization
    ) {
        return organizationUserService.getUsersInOrganization(organization);
    }

    private PagedModel<UserWithRoleResponse> getFilteredUsersFromAuthService(
            UserFilterRequest request, List<OrganizationUser> usersInOrganization
    ) {
        List<UserAndRoles> userAndRoles = usersInOrganization
                .stream()
                .map(organizationUserMapper::toUserAndRoleResponse)
                .toList();

        UserWithRolesFilterRequest rolesFilterRequest = organizationUserMapper.toRolesFilterRequest(request);

        rolesFilterRequest.setUserAndRoles(userAndRoles);

        return userClientWrapper.filterUsers(rolesFilterRequest);
    }

}
