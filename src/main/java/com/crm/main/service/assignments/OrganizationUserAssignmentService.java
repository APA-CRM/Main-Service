package com.crm.main.service.assignments;

import com.crm.main.enums.RoleType;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationRoleService;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.wrapper.UserClientWrapper;
import com.crm.sharedlib.dto.response.UserResponse;
import com.crm.sharedlib.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationUserAssignmentService {

    private final OrganizationUserService organizationUserService;
    private final OrganizationRoleService roleService;
    private final OrganizationRoleUserService roleUserService;

    private final UserClientWrapper userClientWrapper;

    @Transactional
    // TODO: Notify user about adding to organization by email
    public UserResponse addUserToOrganization(
            Organization organization,
            Long userId
    ) {
        Optional<OrganizationUser> organizationUser =
                organizationUserService.getOrganizationUserOptional(organization, userId);

        if (organizationUser.isPresent()) {
            throw new ConflictException("User already in organization");
        }

        UserResponse user = userClientWrapper.getUserById(userId);

        OrganizationUser userOfOrganization =
                organizationUserService.createUserOfOrganization(organization, userId);

        OrganizationRole memberRole =
                roleService.getOrganizationRoleByRoleType(organization, RoleType.MEMBER);

        roleUserService.createRoleForOrganizationUser(memberRole, userOfOrganization);

        return user;
    }

    @Transactional
    public void removeUserFromOrganization(
            Organization organization,
            Long userId
    ) {
        OrganizationUser organizationUser =
                organizationUserService.getOrganizationUser(organization, userId);

        roleUserService.deleteRolesForUser(organizationUser);

        organizationUserService.deleteUserOfOrganization(organizationUser);
    }

}
