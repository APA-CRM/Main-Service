package com.crm.main.service;

import com.crm.main.dto.request.CreateOrganizationRequest;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationCreatorService {

    private final OrganizationRoleService organizationRoleService;
    private final OrganizationService organizationService;
    private final OrganizationUserService organizationUserService;
    private final OrganizationRoleUserService organizationRoleUserService;

    @Transactional
    public Organization createOrganization(
            CreateOrganizationRequest request,
            Long userId,
            Long adminRoleId
    ) {
        Organization organization = organizationService.createOrganization(request);

        OrganizationRole role =
                organizationRoleService.createRoleForOrganization(organization, adminRoleId);

        OrganizationUser userOfOrganization =
                organizationUserService.createUserOfOrganization(organization, userId);

        OrganizationRoleUser roleForOrganizationUser =
                organizationRoleUserService.createRoleForOrganizationUser(role, userOfOrganization);

        return organization;
    }

}
