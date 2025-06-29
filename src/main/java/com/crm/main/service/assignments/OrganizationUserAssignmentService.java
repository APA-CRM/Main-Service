package com.crm.main.service.assignments;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.OrganizationUserService;
import com.crm.sharedlib.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationUserAssignmentService {

    private final OrganizationUserService organizationUserService;
    private final OrganizationRoleUserService roleUserService;

    @Transactional
    // TODO: Notify users of organization about adding new user to organization
    public void addUserToOrganization(
            Organization organization,
            OrganizationRole role,
            Long userId
    ) {
        Optional<OrganizationUser> organizationUser =
                organizationUserService.getOrganizationUserOptional(organization, userId);

        if (organizationUser.isPresent()) {
            throw new ConflictException("User already in organization");
        }

        OrganizationUser userOfOrganization =
                organizationUserService.createUserOfOrganization(organization, userId);

        roleUserService.createRoleForOrganizationUser(role, userOfOrganization);
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
