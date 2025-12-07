package com.crm.main.service.assignments;

import com.crm.main.enums.UserMessage;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationRoleService;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.producer.OrgUserRoleChangedProducer;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import com.crm.sharedlib.core.exception.ForbiddenException;
import com.crm.sharedlib.messaging.service.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

import static com.crm.main.enums.UserMessage.ROLE_HAS_BEEN_ASSIGNED;
import static com.crm.main.enums.UserMessage.ROLE_HAS_BEEN_UNASSIGNED;

@Service
@RequiredArgsConstructor
public class OrganizationUserRoleAssignmentService {

    private final OrganizationUserService userService;
    private final OrganizationRoleService roleService;
    private final OrganizationRoleUserService roleUserService;

    private final RoleClientWrapper roleClientWrapper;

    private final OrgUserRoleChangedProducer roleChangedProducer;

    private final MessagingService messagingService;

    @Transactional
    public RoleResponse addRoleForUserInOrganization(
            Organization organization, Long roleId,
            Long userId, Long authorizedUser
    ) {

        OrganizationRole role = roleService.getOrganizationRole(organization, roleId);

        OrganizationUser organizationUser = userService.getOrganizationUser(organization, userId);

        roleUserService.createRoleForOrganizationUser(role, organizationUser);

        RoleResponse roleResponse = roleClientWrapper.getRole(roleId);

        roleChangedProducer.sendOrgUserRoleChanged(organization, organizationUser);

        if (!Objects.equals(userId, authorizedUser)) {
            UserMessage message = ROLE_HAS_BEEN_ASSIGNED;

            messagingService.sendMessageToUser(
                    userId, message.getTitle(), message.getMessageCode(),
                    message.getMessage().formatted(roleResponse.getName()),
                    Map.of("roleId", roleId)
            );
        }

        return roleResponse;
    }

    @Transactional
    public void removeRoleForUserOrganization(
            Organization organization, Long roleId,
            Long userId, Long authorizedUser
    ) {
        OrganizationRole role = roleService.getOrganizationRole(organization, roleId);

        OrganizationUser organizationUser = userService.getOrganizationUser(organization, userId);

        OrganizationRoleUser roleOfUserOrganization =
                roleUserService.getRoleOfUserOrganization(role, organizationUser);

        if (organizationUser.getOrganizationRoleUsers().size() == 1) {
            throw new ForbiddenException("Role can't be unassigned — user has no other roles");
        }

        RoleResponse roleResponse = roleClientWrapper.getRole(roleId);

        roleUserService.deleteRoleForUser(roleOfUserOrganization);

        roleChangedProducer.sendOrgUserRoleChanged(organization, organizationUser);

        if (!Objects.equals(userId, authorizedUser)) {
            UserMessage messages = ROLE_HAS_BEEN_UNASSIGNED;

            messagingService.sendMessageToUser(
                    userId,
                    messages.getTitle(), messages.getMessageCode(),
                    messages.getMessage().formatted(roleResponse.getName()),
                    Map.of("roleId", roleId)
            );
        }

    }

}
