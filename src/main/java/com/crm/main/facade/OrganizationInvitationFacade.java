package com.crm.main.facade;

import com.crm.main.dto.request.OrganizationInvitationRequest;
import com.crm.main.dto.response.OrganizationInvitationResponse;
import com.crm.main.mapper.OrganizationInvitationMapper;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationInvitation;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.processor.InvitationProcessorService;
import com.crm.sharedlib.annotations.Facade;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class OrganizationInvitationFacade {

    private final InvitationProcessorService invitationProcessorService;
    private final OrganizationService organizationService;

    private final OrganizationInvitationMapper mapper;

    public OrganizationInvitationResponse inviteUserToOrganization(
            OrganizationInvitationRequest request,
            Long organizationId, Long invitorId
    ) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        OrganizationInvitation invitation = invitationProcessorService.inviteUserToOrganization(
                organization, request.getUserId(), invitorId
        );

        return mapper.toDto(invitation);
    }

}
