package com.crm.main.service;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.persistance.repository.OrganizationRoleUserRepository;
import com.crm.sharedlib.exception.ConflictException;
import com.crm.sharedlib.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationRoleUserService {

    private final OrganizationRoleUserRepository repository;

    @Transactional
    public OrganizationRoleUser createRoleForOrganizationUser(
            OrganizationRole organizationRole,
            OrganizationUser organizationUser
    ) {
        Optional<OrganizationRoleUser> roleUser =
                repository.findByOrganizationUserAndOrganizationRole(organizationUser, organizationRole);

        if (roleUser.isPresent()) {
            throw new ConflictException("User already has this role");
        }

        OrganizationRoleUser organizationRoleUser = new OrganizationRoleUser();

        organizationRoleUser.setOrganizationRole(organizationRole);
        organizationRoleUser.setOrganizationUser(organizationUser);

        return repository.save(organizationRoleUser);
    }

    public OrganizationRoleUser getRoleOfUserOrganization(
            OrganizationRole organizationRole,
            OrganizationUser organizationUser
    ) {
        return repository
                .findByOrganizationUserAndOrganizationRole(organizationUser, organizationRole)
                .orElseThrow(() -> new NotFoundException("Role for user is not found"));
    }

    public List<OrganizationRoleUser> filterOrganizationRolesByIds(
            List<Long> rolesId, Organization organization
    ) {
        return repository.findByRolesInAndOrganization(rolesId, organization);
    }

    @Transactional
    public void deleteRoleForUser(OrganizationRoleUser organizationRoleUser) {
        repository.delete(organizationRoleUser);
    }

    @Transactional
    public void deleteRolesForUser(OrganizationUser organizationUser) {
        repository.deleteByOrganizationUser(organizationUser);
    }

    @Transactional
    public void deleteRole(OrganizationRole organizationRole) {
        repository.deleteByOrganizationRole(organizationRole);
    }

}
