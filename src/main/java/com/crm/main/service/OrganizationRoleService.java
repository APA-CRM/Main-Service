package com.crm.main.service;

import com.crm.main.enums.RoleType;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.repository.OrganizationRoleRepository;
import com.crm.sharedlib.exception.ForbiddenException;
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
        return createOrganizationRole(
                organization, roleId, RoleType.USER
        );
    }

    @Transactional
    public OrganizationRole createAdminRoleForOrganization(
            Organization organization,
            Long roleId
    ) {
        return createOrganizationRole(
                organization, roleId, RoleType.ADMIN
        );
    }

    @Transactional
    public OrganizationRole createMemberRoleForOrganization(
            Organization organization,
            Long roleId
    ) {
        return createOrganizationRole(
                organization, roleId, RoleType.MEMBER
        );
    }

    public OrganizationRole getOrganizationRoleByRoleType(
            Organization organization, RoleType roleType
    ) {
        return repository.findByOrganizationAndRoleType(organization, roleType)
                .orElseThrow(() -> new NotFoundException("Role is not found"));
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
        RoleType roleType = organizationRole.getRoleType();

        if (roleType == RoleType.ADMIN || roleType == RoleType.MEMBER) {
            throw new ForbiddenException("You can't delete this role");
        }

        repository.delete(organizationRole);
    }

    private OrganizationRole createOrganizationRole(
            Organization organization, Long roleId,
            RoleType roleType
    ) {
        OrganizationRole role = new OrganizationRole();

        role.setOrganization(organization);
        role.setRoleId(roleId);
        role.setRoleType(roleType);

        return repository.save(role);
    }

}
