package com.crm.main.mapper;

import com.crm.main.dto.response.OrganizationFileResponse;
import com.crm.main.persistance.entity.OrganizationFile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = OrganizationMapper.class
)
public interface OrganizationFileMapper {

    OrganizationFileResponse toDto(OrganizationFile organizationFile);

}
