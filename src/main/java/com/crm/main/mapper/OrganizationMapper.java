package com.crm.main.mapper;

import com.crm.main.dto.request.OrganizationRequest;
import com.crm.main.dto.response.OrganizationPreviewResponse;
import com.crm.main.dto.response.OrganizationResponse;
import com.crm.main.persistance.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper {

    OrganizationResponse toDto(Organization organization);

    OrganizationPreviewResponse toPreviewDto(Organization organization);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizationUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Organization toEntity(OrganizationRequest request);

    Organization updateOrganization(OrganizationRequest request,
                                    @MappingTarget Organization organization);

}
