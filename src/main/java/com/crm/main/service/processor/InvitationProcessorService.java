package com.crm.main.service.processor;

import com.crm.main.enums.InvitationStatus;
import com.crm.main.enums.RoleType;
import com.crm.main.enums.UserMessage;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationInvitation;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationInvitationService;
import com.crm.main.service.OrganizationRoleService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.producer.InvitationCreatedProducer;
import com.crm.main.service.wrapper.UserClientWrapper;
import com.crm.sharedlib.core.dto.response.UserResponse;
import com.crm.sharedlib.core.exception.ConflictException;
import com.crm.sharedlib.core.exception.ForbiddenException;
import com.crm.sharedlib.messaging.service.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.crm.main.enums.UserMessage.*;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class InvitationProcessorService {

    private final OrganizationInvitationService invitationService;
    private final OrganizationUserService userService;
    private final OrganizationRoleService roleService;

    private final UserClientWrapper userClientWrapper;

    private final InvitationCreatedProducer invitationCreatedProducer;

    private final MessagingService messagingService;

    @Transactional
    public OrganizationInvitation inviteUserToOrganization(
            Organization organization, UserResponse user,
            Long invitorId, Long roleId
    ) {
        Long userId = user.getId();

        Optional<OrganizationUser> userOptional =
                userService.getOrganizationUserOptional(organization, userId);

        if (userOptional.isPresent()) {
            throw new ConflictException("User already in organization");
        }

        OrganizationRole role;

        if (isNull(roleId)) {
            role = roleService.getOrganizationRoleByRoleType(organization, RoleType.MEMBER);
        } else {
            role = roleService.getOrganizationRole(organization, roleId);
        }

        OrganizationInvitation invitation = invitationService.createInvitation(
                organization, userId, invitorId, role
        );

        invitationCreatedProducer
                .notifyUserAboutInvitationOfOrganization(invitation, user.getEmail());

        UserMessage message = USER_HAS_BEEN_INVITED_TO_ORGANIZATION;

        messagingService.sendMessageToUser(
                invitation.getUserId(), message.getTitle(), message.getMessageCode(),
                message.getMessage().formatted(invitation.getOrganization().getName()),
                Map.of("invitationId", invitation.getId())
        );

        return invitation;
    }

    @Transactional
    public OrganizationInvitation acceptInvitation(UUID invitationId, Long userId) {
        OrganizationInvitation invitation =
                getAndUpdateInvitationStatus(invitationId, userId, InvitationStatus.ACCEPTED);

        UserResponse user = userClientWrapper.getUserById(userId);

        UserMessage message = USER_HAS_ACCEPTED_AN_INVITATION;

        messagingService.sendMessageToUser(
                invitation.getInvitorId(), message.getTitle(), message.getMessageCode(),
                message.getMessage().formatted(
                        user.getFullName(), invitation.getOrganization().getName()
                ),
                Map.of("userId", userId, "invitationId", invitation.getId())
        );

        return invitation;
    }

    @Transactional
    public OrganizationInvitation declineInvitation(UUID invitationId, Long userId) {
        OrganizationInvitation invitation =
                getAndUpdateInvitationStatus(invitationId, userId, InvitationStatus.DECLINED);

        UserResponse user = userClientWrapper.getUserById(userId);

        UserMessage message = USER_HAS_DECLINED_AN_INVITATION;

        messagingService.sendMessageToUser(
                invitation.getInvitorId(), message.getTitle(), message.getMessageCode(),
                message.getMessage().formatted(
                        user.getFullName(), invitation.getOrganization().getName()
                ),
                Map.of("userId", userId, "invitationId", invitation.getId())
        );

        return invitation;
    }

    private OrganizationInvitation getAndUpdateInvitationStatus(
            UUID invitationId, Long userId, InvitationStatus status
    ) {
        OrganizationInvitation invitation =
                invitationService.getOrganizationInvitationOrThrowException(invitationId);

        validateOrganizationInvitation(invitation, userId);

        invitation.setStatus(status);

        return invitationService.saveOrganizationInvitation(invitation);
    }

    private void validateOrganizationInvitation(OrganizationInvitation invitation, Long userId) {
        if (!invitation.getUserId().equals(userId)) {
            throw new ForbiddenException("You can't accept this invitation");
        }
        if (invitation.getExpiredAt().isBefore(Instant.now())) {
            throw new ForbiddenException("Invitation time has expired");
        }
        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new ConflictException("Invitation is already accepted");
        }
        if (invitation.getStatus() == InvitationStatus.DECLINED) {
            throw new ConflictException("Invitation is already declined");
        }
    }
}
