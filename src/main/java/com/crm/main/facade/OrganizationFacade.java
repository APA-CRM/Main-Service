package com.crm.main.facade;

import com.crm.main.dto.request.CreateOrganizationRequest;
import com.crm.main.dto.response.OrganizationPreviewResponse;
import com.crm.main.dto.response.OrganizationResponse;
import com.crm.main.mapper.OrganizationMapper;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.operation.OrganizationCreatorService;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.sharedlib.annotations.Facade;
import com.crm.sharedlib.dto.response.RoleResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class OrganizationFacade {

    private final OrganizationService organizationService;

    private final OrganizationUserService organizationUserService;

    private final OrganizationCreatorService organizationCreatorService;

    private final RoleClientWrapper roleClientWrapper;

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
            CreateOrganizationRequest request,
            Long userId
    ) {
        RoleResponse role = roleClientWrapper.createAdminRoleForOrganization();

        Organization organization = organizationCreatorService
                .createOrganization(request, userId, role.getId());

        return organizationMapper.toDto(organization);
    }

}
