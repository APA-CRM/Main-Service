package com.crm.main.service.assignments;

import com.crm.main.enums.OrganizationMessage;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.producer.OrgUserRoleChangedProducer;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.main.service.wrapper.UserClientWrapper;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import com.crm.sharedlib.core.dto.response.UserResponse;
import com.crm.sharedlib.core.exception.ConflictException;
import com.crm.sharedlib.messaging.service.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.crm.main.enums.OrganizationMessage.USER_HAS_BEEN_ADDED_TO_THE_ORGANIZATION;

@Service
@RequiredArgsConstructor
public class OrganizationUserAssignmentService {

    private final OrganizationUserService organizationUserService;
    private final OrganizationRoleUserService roleUserService;

    private final UserClientWrapper userClientWrapper;
    private final RoleClientWrapper roleClientWrapper;

    private final OrgUserRoleChangedProducer roleChangedProducer;

    private final MessagingService messagingService;

    @Transactional
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

        roleChangedProducer.sendOrgUserRoleChanged(organization, userOfOrganization);

        notifyOrganizationAboutAddedUser(organization.getId(), userId, role.getRoleId());
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

    private void notifyOrganizationAboutAddedUser(Long organizationId, Long userId, Long roleId) {

        UserResponse user = userClientWrapper.getUserById(userId);
        RoleResponse role = roleClientWrapper.getRole(roleId);

        OrganizationMessage message = USER_HAS_BEEN_ADDED_TO_THE_ORGANIZATION;

        messagingService.sendMessageToOrganization(
                organizationId, message.getTitle(), message.getMessageCode(),
                message.getMessage().formatted(user.getFullName(), role.getName()),
                Map.of("userId", userId)
        );

    }

}
