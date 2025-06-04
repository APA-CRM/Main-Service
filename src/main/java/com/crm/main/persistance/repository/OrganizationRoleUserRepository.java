package com.crm.main.persistance.repository;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRoleUserRepository
        extends JpaRepository<OrganizationRoleUser, UUID> {

    @Query(
            """
                    SELECT oru FROM OrganizationRoleUser oru
                    WHERE oru.organizationRole.roleId IN :rolesId
                    AND oru.organizationRole.organization = :organization
                    """
    )
    @EntityGraph(attributePaths = {"organizationRole", "organizationUser"})
    List<OrganizationRoleUser> findByRolesInAndOrganization(
            @Param("rolesId") List<Long> rolesId,
            @Param("organization") Organization organization
    );

    Optional<OrganizationRoleUser> findByOrganizationUserAndOrganizationRole(
            OrganizationUser organizationUser,
            OrganizationRole organizationRole
    );

    List<OrganizationRoleUser> findByOrganizationUser(OrganizationUser organizationUser);

    void deleteByOrganizationUser(OrganizationUser organizationUser);

    void deleteByOrganizationRole(OrganizationRole organizationRole);
}
