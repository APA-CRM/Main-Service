package com.crm.main.service;

import com.crm.main.enums.InvitationStatus;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationInvitation;
import com.crm.main.persistance.repository.OrganizationInvitationRepository;
import com.crm.sharedlib.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
            Long invitorId
    ) {
        OrganizationInvitation invitation = new OrganizationInvitation();

        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setOrganization(organization);
        invitation.setUserId(userId);
        invitation.setInvitorId(invitorId);
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
