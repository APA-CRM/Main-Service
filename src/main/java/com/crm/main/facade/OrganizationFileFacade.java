package com.crm.main.facade;

import com.crm.main.dto.response.OrganizationFileResponse;
import com.crm.main.mapper.OrganizationFileMapper;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationFile;
import com.crm.main.service.OrganizationFileService;
import com.crm.main.service.OrganizationService;
import com.crm.sharedlib.annotations.Facade;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Facade
@RequiredArgsConstructor
public class OrganizationFileFacade {

    private final OrganizationFileService fileService;
    private final OrganizationService organizationService;

    private final OrganizationFileMapper fileMapper;

    public OrganizationFileResponse getOrganizationFile(Long organizationId, UUID fileId) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        OrganizationFile organizationFile =
                fileService.getOrganizationFileOrThrowException(organization, fileId);

        return fileMapper.toDto(organizationFile);
    }

    public OrganizationFileResponse getRootOrganizationFile(Long organizationId) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        OrganizationFile rootFile = fileService.getRootOrganizationFileOrThrowException(organization);

        return fileMapper.toDto(rootFile);
    }

    public OrganizationFileResponse createOrganizationFile(Long organizationId, UUID fileId) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        OrganizationFile organizationFile =
                fileService.createOrganizationFile(organization, fileId);

        return fileMapper.toDto(organizationFile);
    }

    public void deleteOrganizationFile(Long organizationId, UUID fileId) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        OrganizationFile organizationFile =
                fileService.getOrganizationFileOrThrowException(organization, fileId);

        fileService.deleteOrganizationFile(organizationFile);
    }

}
