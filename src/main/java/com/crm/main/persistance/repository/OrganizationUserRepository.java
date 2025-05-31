package com.crm.main.persistance.repository;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, UUID> {

    Optional<OrganizationUser> findByOrganizationAndUserId(Organization organization, Long userId);

    Page<OrganizationUser> findByOrganization(
            Organization organization, Pageable pageable
    );

    List<OrganizationUser> findByUserId(Long userId);

}
