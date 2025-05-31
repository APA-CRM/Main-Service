package com.crm.main.persistance.repository;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRoleRepository
        extends JpaRepository<OrganizationRole, UUID> {

    List<OrganizationRole> findByOrganization(Organization organization);

    Page<OrganizationRole> findByOrganization(Organization organization, Pageable pageable);

    Optional<OrganizationRole> findByOrganizationAndRoleId(Organization organization, Long roleId);

}
