package com.crm.main.mapper;

import com.crm.main.dto.response.OrganizationInvitationResponse;
import com.crm.main.persistance.entity.OrganizationInvitation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = OrganizationMapper.class
)
public interface OrganizationInvitationMapper {

    OrganizationInvitationResponse toDto(OrganizationInvitation invitation);

}
