package com.crm.main.controller;

import com.crm.main.annotations.OrganizationId;
import com.crm.main.annotations.RequiresOrganizationMembership;
import com.crm.main.annotations.UserId;
import com.crm.main.dto.response.OrganizationFileResponse;
import com.crm.main.facade.OrganizationFileFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationFileController {

    private final OrganizationFileFacade facade;

    @GetMapping("/{organizationId}/files/{fileId}")
    @RequiresOrganizationMembership
    public OrganizationFileResponse getOrganizationFile(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId,
            @PathVariable("fileId") UUID fileId
    ) {
        return facade.getOrganizationFile(organizationId, fileId);
    }

    @GetMapping("/{organizationId}/files/root")
    @RequiresOrganizationMembership
    public OrganizationFileResponse getRootOrganizationFile(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        return facade.getRootOrganizationFile(organizationId);
    }

    @PostMapping("/{organizationId}/files/{fileId}")
    @RequiresOrganizationMembership
    public OrganizationFileResponse createOrganizationFile(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @PathVariable("fileId") UUID fileId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        return facade.createOrganizationFile(organizationId, fileId);
    }

    @DeleteMapping("/{organizationId}/files/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiresOrganizationMembership
    public void deleteOrganizationFile(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @PathVariable("fileId") UUID fileId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        facade.deleteOrganizationFile(organizationId, fileId);
    }

}
