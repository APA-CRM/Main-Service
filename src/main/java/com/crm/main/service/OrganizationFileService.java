package com.crm.main.service;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationFile;
import com.crm.main.persistance.repository.OrganizationFileRepository;
import com.crm.sharedlib.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationFileService {

    private final OrganizationFileRepository fileRepository;

    public OrganizationFile getOrThrowException(Organization organization, UUID fileId) {
        return fileRepository.findByOrganizationAndFileId(organization, fileId)
                .orElseThrow(() -> new NotFoundException("Organization file is not found"));
    }

    public OrganizationFile getOrThrowException(UUID id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Organization file is not found"));
    }

    @Transactional
    public OrganizationFile createOrganizationFile(Organization organization, UUID fileId) {
        OrganizationFile file = new OrganizationFile();
        file.setOrganization(organization);
        file.setFileId(fileId);

        return fileRepository.save(file);
    }

    @Transactional
    public void deleteOrganizationFile(OrganizationFile file) {
        fileRepository.delete(file);
    }

}
