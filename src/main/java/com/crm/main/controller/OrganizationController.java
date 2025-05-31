package com.crm.main.controller;

import com.crm.main.dto.request.CreateOrganizationRequest;
import com.crm.main.dto.response.OrganizationPreviewResponse;
import com.crm.main.dto.response.OrganizationResponse;
import com.crm.main.facade.OrganizationFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;

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
            CreateOrganizationRequest request,
            @RequestHeader(USER_ID_HEADER_NAME)
            Long userId
    ) {
        return facade.createOrganization(request, userId);
    }

}
