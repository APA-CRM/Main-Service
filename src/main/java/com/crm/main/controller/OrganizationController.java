package com.crm.main.controller;

import com.crm.main.annotations.OrganizationId;
import com.crm.main.annotations.RequiresOrganizationMembership;
import com.crm.main.annotations.UserId;
import com.crm.main.dto.request.OrganizationRequest;
import com.crm.main.dto.response.OrganizationPreviewResponse;
import com.crm.main.dto.response.OrganizationResponse;
import com.crm.main.facade.OrganizationFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationFacade facade;

    @GetMapping
    public List<OrganizationPreviewResponse> getOrganizationsOfUser(
            @RequestHeader(USER_ID_HEADER_NAME)
            Long userId
    ) {
        return facade.getOrganizationsOfUser(userId);
    }

    @GetMapping("/{organizationId}/preview")
    public OrganizationPreviewResponse getOrganizationPreview(
            @PathVariable("organizationId")
            Long organizationId
    ) {
        return facade.getOrganizationPreview(organizationId);
    }

    @PostMapping
    public OrganizationResponse createOrganization(
            @Valid
            @RequestBody
            OrganizationRequest request,
            @RequestHeader(USER_ID_HEADER_NAME)
            Long userId
    ) {
        return facade.createOrganization(request, userId);
    }

    @GetMapping("/{organizationId}")
    public OrganizationResponse getOrganizationById(
            @PathVariable("organizationId")
            Long organizationId
    ) {
        return facade.getOrganizationById(organizationId);
    }

    @PutMapping("/{organizationId}")
    @RequiresOrganizationMembership
    public OrganizationResponse updateOrganizationById(
            @OrganizationId @PathVariable("organizationId")
            Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId,
            @RequestBody OrganizationRequest request
    ) {
        return facade.updateOrganizationById(request, organizationId);
    }
}
