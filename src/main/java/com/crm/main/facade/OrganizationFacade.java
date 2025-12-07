package com.crm.main.facade;

import com.crm.main.dto.request.OrganizationRequest;
import com.crm.main.dto.response.OrganizationPreviewResponse;
import com.crm.main.dto.response.OrganizationResponse;
import com.crm.main.mapper.OrganizationMapper;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.operation.OrganizationCreatorService;
import com.crm.sharedlib.core.annotations.Facade;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class OrganizationFacade {

    private final OrganizationService organizationService;

    private final OrganizationUserService organizationUserService;

    private final OrganizationCreatorService organizationCreatorService;

    private final OrganizationMapper organizationMapper;

    public List<OrganizationPreviewResponse> getOrganizationsOfUser(Long userId) {
        List<OrganizationUser> organizationOfUser =
                organizationUserService.getAllOrganizationsOfUser(userId);

        return organizationOfUser.stream()
                .map(OrganizationUser::getOrganization)
                .map(organizationMapper::toPreviewDto)
                .toList();
    }

    public OrganizationPreviewResponse getOrganizationPreview(Long organizationId) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        return organizationMapper.toPreviewDto(organization);
    }

    public OrganizationResponse createOrganization(
            OrganizationRequest request,
            Long userId
    ) {
        Organization organization = organizationCreatorService
                .createOrganization(request, userId);

        return organizationMapper.toDto(organization);
    }

    public OrganizationResponse getOrganizationById(Long organizationId) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        return organizationMapper.toDto(organization);
    }

    public OrganizationResponse updateOrganizationById(
            OrganizationRequest request,
            Long organizationId
    ) {
        Organization organization = organizationService.updateOrganization(organizationId, request);

        return organizationMapper.toDto(organization);
    }
}
