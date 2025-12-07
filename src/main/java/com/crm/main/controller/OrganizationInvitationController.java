package com.crm.main.controller;

import com.crm.main.dto.request.OrganizationInvitationRequest;
import com.crm.main.dto.response.OrganizationInvitationResponse;
import com.crm.main.facade.OrganizationInvitationFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationInvitationController {

    private final OrganizationInvitationFacade facade;

    @GetMapping("/invitations/{invitationId}")
    public OrganizationInvitationResponse getInvitation(
            @PathVariable("invitationId") UUID invitationId
    ) {
        return facade.getInvitation(invitationId);
    }

    @PostMapping("/{organizationId}/invitations")
    public OrganizationInvitationResponse inviteUserToOrganization(
            @Valid
            @RequestBody
            OrganizationInvitationRequest request,
            @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId
    ) {
        return facade.inviteUserToOrganization(request, organizationId, userId);
    }

    @PatchMapping("/invitations/{invitationId}/accept")
    public OrganizationInvitationResponse acceptInvitation(
            @PathVariable("invitationId") UUID invitationId,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId
    ) {
        return facade.acceptInvitation(invitationId, userId);
    }

    @PatchMapping("/invitations/{invitationId}/decline")
    public OrganizationInvitationResponse declineInvitation(
            @PathVariable("invitationId") UUID invitationId,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId
    ) {
        return facade.declineInvitation(invitationId, userId);
    }

}
