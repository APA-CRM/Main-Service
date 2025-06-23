package com.crm.main.service.processor;

import com.crm.main.enums.InvitationStatus;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationInvitation;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationInvitationService;
import com.crm.main.service.OrganizationUserService;
import com.crm.sharedlib.exception.ConflictException;
import com.crm.sharedlib.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationProcessorService {

    private final OrganizationInvitationService invitationService;
    private final OrganizationUserService userService;

    @Transactional
    public OrganizationInvitation inviteUserToOrganization(
            Organization organization, Long userId,
            Long invitorId
    ) {

        Optional<OrganizationUser> userOptional =
                userService.getOrganizationUserOptional(organization, userId);

        if (userOptional.isPresent()) {
            throw new ConflictException("User already in organization");
        }

        return invitationService.createInvitation(organization, userId, invitorId);
    }

    @Transactional
    public OrganizationInvitation acceptInvitation(UUID invitationId, Long userId) {
        return getAndUpdateInvitationStatus(invitationId, userId, InvitationStatus.ACCEPTED);
    }

    @Transactional
    public OrganizationInvitation declineInvitation(UUID invitationId, Long userId) {
        return getAndUpdateInvitationStatus(invitationId, userId, InvitationStatus.DECLINED);
    }

    private OrganizationInvitation getAndUpdateInvitationStatus(
            UUID invitationId, Long userId, InvitationStatus status
    ) {
        OrganizationInvitation invitation =
                invitationService.getOrganizationInvitationOrThrowException(invitationId);

        if (!invitation.getUserId().equals(userId)) {
            throw new ForbiddenException("You can't accept this invitation");
        }

        if (invitation.getExpiredAt().isBefore(Instant.now())) {
            throw new ForbiddenException("Invitation time has expired");
        }

        invitation.setStatus(status);

        return invitationService.saveOrganizationInvitation(invitation);
    }

}
