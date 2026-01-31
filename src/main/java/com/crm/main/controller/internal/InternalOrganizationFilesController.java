package com.crm.main.controller.internal;

import com.crm.main.annotations.OrganizationId;
import com.crm.main.annotations.RequiresOrganizationMembership;
import com.crm.main.annotations.UserId;
import com.crm.main.facade.internal.InternalOrganizationFilesFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/api/internal/organizations")
@RequiredArgsConstructor
public class InternalOrganizationFilesController {

    private final InternalOrganizationFilesFacade facade;

    @GetMapping("/{organizationId}/files/{fileId}/check")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiresOrganizationMembership
    public void organizationHasAFile(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @PathVariable("fileId") UUID fileId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId
            Long userId
    ) {
        facade.organizationHasAFile(organizationId, fileId);
    }
}
