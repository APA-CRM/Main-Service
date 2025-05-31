package com.crm.main.service;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.persistance.repository.OrganizationUserRepository;
import com.crm.sharedlib.exception.ForbiddenException;
import com.crm.sharedlib.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationUserService {

    private final OrganizationUserRepository repository;

    //TODO: Think about using interceptor instead of manual invoking this method
    public void getIsUserInOrganizationOrThrowException(
            Organization organization,
            Long userId
    ) {
        repository.findByOrganizationAndUserId(organization, userId)
                .orElseThrow(() -> new ForbiddenException("User is not in the organization"));
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

    public Page<OrganizationUser> getUsersInOrganization(
            Organization organization,
            Integer page, Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return repository.findByOrganization(organization, pageable);
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
