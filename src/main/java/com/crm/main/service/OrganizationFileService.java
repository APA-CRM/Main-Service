package com.crm.main.service;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationFile;
import com.crm.main.persistance.repository.OrganizationFileRepository;
import com.crm.sharedlib.exception.ConflictException;
import com.crm.sharedlib.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationFileService {

    private final OrganizationFileRepository fileRepository;

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
    public void deleteOrganizationFile(OrganizationFile file) {
        fileRepository.delete(file);
    }

}
