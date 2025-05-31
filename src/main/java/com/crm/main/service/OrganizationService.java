package com.crm.main.service;

import com.crm.main.dto.request.CreateOrganizationRequest;
import com.crm.main.mapper.OrganizationMapper;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.repository.OrganizationRepository;
import com.crm.sharedlib.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    public Organization getOrganizationOrThrowException(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization is not found"));
    }

    @Transactional
    public Organization createOrganization(CreateOrganizationRequest request) {
        Organization organization = organizationMapper.toEntity(request);

        return organizationRepository.save(organization);
    }

}
