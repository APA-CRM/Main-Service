package com.crm.main.service;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.repository.OrganizationRoleRepository;
import com.crm.sharedlib.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationRoleService {

    private final OrganizationRoleRepository repository;

    @Transactional
    public OrganizationRole createRoleForOrganization(
            Organization organization,
            Long roleId
    ) {
        OrganizationRole role = new OrganizationRole();

        role.setOrganization(organization);
        role.setRoleId(roleId);

        return repository.save(role);
    }

    public OrganizationRole getOrganizationRole(Organization organization, Long roleId) {
        return repository.findByOrganizationAndRoleId(organization, roleId)
                .orElseThrow(() -> new NotFoundException("Role is not found"));
    }

    public List<OrganizationRole> getOrganizationsRoles(Organization organization) {
        return repository.findByOrganization(organization);
    }

    @Transactional
    public void deleteOrganizationRole(OrganizationRole organizationRole) {
        repository.delete(organizationRole);
    }

}
