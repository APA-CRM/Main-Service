package com.crm.main.persistance.repository;

import com.crm.main.enums.InvitationStatus;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationInvitationRepository extends JpaRepository<OrganizationInvitation, UUID> {

    Optional<OrganizationInvitation> findByUserIdAndOrganizationAndStatus(
            Long userId, Organization organization, InvitationStatus status
    );

}
