package com.crm.main.persistance.repository;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationFileRepository extends JpaRepository<OrganizationFile, UUID> {

    Optional<OrganizationFile> findByOrganizationAndFileId(Organization organization, UUID fileId);

    boolean existsByOrganizationAndFileId(Organization organization, UUID fileId);
}
