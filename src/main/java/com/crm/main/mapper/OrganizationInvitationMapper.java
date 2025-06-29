package com.crm.main.mapper;

import com.crm.main.dto.response.OrganizationInvitationResponse;
import com.crm.main.persistance.entity.OrganizationInvitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = OrganizationMapper.class
)
public interface OrganizationInvitationMapper {

    @Mapping(target = "roleId", source = "role.roleId")
    OrganizationInvitationResponse toDto(OrganizationInvitation invitation);

}
