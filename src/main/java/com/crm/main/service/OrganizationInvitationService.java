package com.crm.main.service;

import com.crm.main.enums.InvitationStatus;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationInvitation;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.repository.OrganizationInvitationRepository;
import com.crm.sharedlib.core.exception.ConflictException;
import com.crm.sharedlib.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationInvitationService {

    private final OrganizationInvitationRepository repository;

    @Value("${app.invitation.expiration-time}")
    private Long invitationExpirationTime;

    @Transactional
    public OrganizationInvitation createInvitation(
            Organization organization, Long userId,
            Long invitorId, OrganizationRole role
    ) {
        Optional<OrganizationInvitation> existingInvitation =
                repository
                        .findByUserIdAndOrganizationAndStatus(userId, organization, InvitationStatus.PENDING);

        if (existingInvitation.isPresent()) {
            throw new ConflictException("Invitation is already created");
        }

        OrganizationInvitation invitation = new OrganizationInvitation();

        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setOrganization(organization);
        invitation.setUserId(userId);
        invitation.setInvitorId(invitorId);
        invitation.setRole(role);
        invitation.setExpiredAt(Instant.now().plusSeconds(invitationExpirationTime));

        return repository.save(invitation);
    }

    @Transactional
    public OrganizationInvitation saveOrganizationInvitation(OrganizationInvitation invitation) {
        return repository.save(invitation);
    }

    public OrganizationInvitation getOrganizationInvitationOrThrowException(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invitation is not found"));
    }

}
