package com.crm.main.service;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.persistance.repository.OrganizationUserRepository;
import com.crm.sharedlib.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationUserService {

    private final OrganizationUserRepository repository;

    public Optional<OrganizationUser> getByOrganizationAndUserId(
            Long organizationId,
            Long userId
    ) {
        return repository.findByOrganizationIdAndUserId(organizationId, userId);
    }

    public Optional<OrganizationUser> getOrganizationUserOptional(
            Organization organization,
            Long userId
    ) {
        return repository.findByOrganizationAndUserId(organization, userId);
    }

    public OrganizationUser getOrganizationUser(
            Organization organization,
            Long userId
    ) {
        return repository.findByOrganizationAndUserId(organization, userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
    }

    public List<OrganizationUser> getUsersInOrganization(Organization organization) {
        return repository.findByOrganization(organization);
    }

    public List<OrganizationUser> getAllOrganizationsOfUser(Long userId) {
        return repository.findByUserId(userId);
    }

    @Transactional
    public OrganizationUser createUserOfOrganization(
            Organization organization,
            Long userId
    ) {
        OrganizationUser organizationUser = new OrganizationUser();

        organizationUser.setOrganization(organization);
        organizationUser.setUserId(userId);

        return repository.save(organizationUser);
    }

    @Transactional
    public void deleteUserOfOrganization(OrganizationUser organizationUser) {
        repository.delete(organizationUser);
    }
}
