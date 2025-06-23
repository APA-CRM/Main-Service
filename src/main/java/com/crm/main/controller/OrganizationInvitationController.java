package com.crm.main.controller;

import com.crm.main.dto.request.OrganizationInvitationRequest;
import com.crm.main.dto.response.OrganizationInvitationResponse;
import com.crm.main.facade.OrganizationInvitationFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationInvitationController {

    private final OrganizationInvitationFacade facade;

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

}
