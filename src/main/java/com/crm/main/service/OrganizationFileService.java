package com.crm.main.service;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationFile;
import com.crm.main.persistance.repository.OrganizationFileRepository;
import com.crm.sharedlib.exception.ConflictException;
import com.crm.sharedlib.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationFileService {

    private final OrganizationFileRepository fileRepository;

    public OrganizationFile getRootOrganizationFileOrThrowException(Organization organization) {
        return fileRepository.findByOrganizationAndIsRootTrue(organization)
                .orElseThrow(() -> new NotFoundException("Organization`s root file is not found"));
    }

    public OrganizationFile getOrganizationFileOrThrowException(Organization organization, UUID fileId) {
        return fileRepository.findByOrganizationAndFileId(organization, fileId)
                .orElseThrow(() -> new NotFoundException("Organization file is not found"));
    }

    @Transactional
    public OrganizationFile createOrganizationFile(Organization organization, UUID fileId) {
        if (fileRepository.existsByOrganizationAndFileId(organization, fileId)) {
            throw new ConflictException("File already in the organization");
        }

        OrganizationFile file = new OrganizationFile();
        file.setOrganization(organization);
        file.setFileId(fileId);

        return fileRepository.save(file);
    }

    @Transactional
    public void createRootOrganizationFile(Organization organization, UUID fileId) {
        Optional<OrganizationFile> rootFile =
                fileRepository.findByOrganizationAndIsRootTrue(organization);

        if (rootFile.isPresent()) {
            throw new ConflictException("Organization is already has a root file");
        }

        OrganizationFile file = new OrganizationFile();

        file.setFileId(fileId);
        file.setIsRoot(true);
        file.setOrganization(organization);

        fileRepository.save(file);
    }

    @Transactional
    public void deleteOrganizationFile(OrganizationFile file) {
        fileRepository.delete(file);
    }

}
