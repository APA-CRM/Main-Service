package com.crm.main.facade;

import com.crm.main.dto.request.OrganizationInvitationRequest;
import com.crm.main.dto.response.OrganizationInvitationResponse;
import com.crm.main.mapper.OrganizationInvitationMapper;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationInvitation;
import com.crm.main.service.OrganizationInvitationService;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.assignments.OrganizationUserAssignmentService;
import com.crm.main.service.processor.InvitationProcessorService;
import com.crm.main.service.wrapper.UserClientWrapper;
import com.crm.sharedlib.annotations.Facade;
import com.crm.sharedlib.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Facade
@RequiredArgsConstructor
public class OrganizationInvitationFacade {

    private final InvitationProcessorService invitationProcessorService;
    private final OrganizationUserAssignmentService assignmentService;
    private final OrganizationService organizationService;
    private final OrganizationInvitationService invitationService;

    private final UserClientWrapper userClientWrapper;

    private final OrganizationInvitationMapper mapper;

    public OrganizationInvitationResponse getInvitation(UUID invitationId) {
        OrganizationInvitation invitation =
                invitationService.getOrganizationInvitationOrThrowException(invitationId);

        return mapper.toDto(invitation);
    }

    public OrganizationInvitationResponse inviteUserToOrganization(
            OrganizationInvitationRequest request,
            Long organizationId, Long invitorId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        UserResponse user = userClientWrapper.getUserById(request.getUserId());

        OrganizationInvitation invitation = invitationProcessorService.inviteUserToOrganization(
                organization, user, invitorId
        );

        return mapper.toDto(invitation);
    }

    public OrganizationInvitationResponse acceptInvitation(UUID invitationId, Long userId) {
        OrganizationInvitation invitation =
                invitationProcessorService.acceptInvitation(invitationId, userId);

        assignmentService.addUserToOrganization(invitation.getOrganization(), userId);

        return mapper.toDto(invitation);
    }

    public OrganizationInvitationResponse declineInvitation(UUID invitationId, Long userId) {
        OrganizationInvitation invitation =
                invitationProcessorService.declineInvitation(invitationId, userId);

        return mapper.toDto(invitation);
    }

}
