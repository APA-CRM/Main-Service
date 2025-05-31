package com.crm.main.mapper;

import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.sharedlib.dto.request.UserFilterRequest;
import com.crm.sharedlib.dto.request.UserWithRolesFilterRequest;
import com.crm.sharedlib.dto.response.UserAndRoles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class OrganizationUserMapper {

    @Mapping(target = "userAndRoles", ignore = true)
    public abstract UserWithRolesFilterRequest toRolesFilterRequest(UserFilterRequest request);

    @Mapping(expression = "java(getRolesOfUsers(organizationUser))", target = "rolesIds")
    public abstract UserAndRoles toUserAndRoleResponse(OrganizationUser organizationUser);

    public List<Long> getRolesOfUsers(OrganizationUser organizationUser) {
        return organizationUser.getOrganizationRoleUsers().stream()
                .map(r -> r.getOrganizationRole().getRoleId())
                .toList();
    }
}
