package com.crm.main.facade.internal;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.service.OrganizationFileService;
import com.crm.main.service.OrganizationService;
import com.crm.sharedlib.core.annotations.Facade;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Facade
@RequiredArgsConstructor
public class InternalOrganizationFilesFacade {

    private final OrganizationService organizationService;
    private final OrganizationFileService fileService;

    public void organizationHasAFile(Long organizationId, UUID fileId) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        fileService.getOrganizationFileOrThrowException(organization, fileId);
    }

}
