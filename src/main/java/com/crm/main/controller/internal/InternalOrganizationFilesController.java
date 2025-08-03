package com.crm.main.controller.internal;

import com.crm.main.facade.internal.InternalOrganizationFilesFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/internal/organizations")
@RequiredArgsConstructor
public class InternalOrganizationFilesController {

    private final InternalOrganizationFilesFacade facade;

    @GetMapping("/{organizationId}/files/{fileId}")
    public ResponseEntity<?> organizationHasAFile(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("fileId") UUID fileId
    ) {
        facade.organizationHasAFile(organizationId, fileId);

        return ResponseEntity.noContent().build();
    }
}
