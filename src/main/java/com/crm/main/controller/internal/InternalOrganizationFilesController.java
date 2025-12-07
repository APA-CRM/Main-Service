package com.crm.main.controller.internal;

import com.crm.main.facade.internal.InternalOrganizationFilesFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/internal/organizations")
@RequiredArgsConstructor
public class InternalOrganizationFilesController {

    private final InternalOrganizationFilesFacade facade;

    @GetMapping("/{organizationId}/files/{fileId}/check")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void organizationHasAFile(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("fileId") UUID fileId
    ) {
        facade.organizationHasAFile(organizationId, fileId);
    }
}
