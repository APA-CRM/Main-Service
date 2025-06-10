package com.crm.main.service.operation;

import com.crm.main.dto.request.CreateOrganizationRequest;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationRoleService;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.OrganizationUserService;
import com.crm.main.service.producer.OrgUserRoleChangedProducer;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.sharedlib.dto.request.ResourceWithActionsRequest;
import com.crm.sharedlib.dto.request.RoleRequest;
import com.crm.sharedlib.dto.response.RoleResponse;
import com.crm.sharedlib.enums.Action;
import com.crm.sharedlib.enums.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OrganizationCreatorService {

    private final OrganizationRoleService organizationRoleService;
    private final OrganizationService organizationService;
    private final OrganizationUserService organizationUserService;
    private final OrganizationRoleUserService organizationRoleUserService;

    private final RoleClientWrapper roleClientWrapper;

    private final OrgUserRoleChangedProducer roleChangedProducer;

    @Value("${app.roles.default-admin-name}")
    private String adminRoleName;

    @Transactional
    public Organization createOrganization(
            CreateOrganizationRequest request,
            Long userId
    ) {
        Organization organization = organizationService.createOrganization(request);

        RoleResponse adminRole = createAdminRoleForOrganization();

        OrganizationRole role =
                organizationRoleService.createRoleForOrganization(organization, adminRole.getId());

        OrganizationUser userOfOrganization =
                organizationUserService.createUserOfOrganization(organization, userId);

        OrganizationRoleUser roleForOrganizationUser =
                organizationRoleUserService.createRoleForOrganizationUser(role, userOfOrganization);

        roleChangedProducer.sendOrgUserRoleChanged(organization, userOfOrganization);

        return organization;
    }

    private RoleResponse createAdminRoleForOrganization() {
        RoleRequest request = new RoleRequest();

        request.setName(adminRoleName);

        ResourceWithActionsRequest resource = new ResourceWithActionsRequest();
        resource.setResource(Resource.ALL);
        resource.setActions(Collections.singletonList(Action.ALL));

        request.setResources(Collections.singletonList(resource));

        return roleClientWrapper.createRole(request);
    }

}
