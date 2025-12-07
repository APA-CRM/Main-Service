package com.crm.main.controller;

import com.crm.main.dto.response.OrganizationFileResponse;
import com.crm.main.facade.OrganizationFileFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationFileController {

    private final OrganizationFileFacade facade;

    @GetMapping("/{organizationId}/files/{fileId}")
    public OrganizationFileResponse getOrganizationFile(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("fileId") UUID fileId
    ) {
        return facade.getOrganizationFile(organizationId, fileId);
    }

    @GetMapping("/{organizationId}/files/root")
    public OrganizationFileResponse getRootOrganizationFile(
            @PathVariable("organizationId") Long organizationId
    ) {
        return facade.getRootOrganizationFile(organizationId);
    }

    @PostMapping("/{organizationId}/files/{fileId}")
    public OrganizationFileResponse createOrganizationFile(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("fileId") UUID fileId
    ) {
        return facade.createOrganizationFile(organizationId, fileId);
    }

    @DeleteMapping("/{organizationId}/files/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganizationFile(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("fileId") UUID fileId
    ) {
        facade.deleteOrganizationFile(organizationId, fileId);
    }

}
