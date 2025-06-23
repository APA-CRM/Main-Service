package com.crm.main.service.processor;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationInvitation;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationInvitationService;
import com.crm.main.service.OrganizationUserService;
import com.crm.sharedlib.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

}
