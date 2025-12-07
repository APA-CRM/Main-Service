package com.crm.main.service.operation;

import com.crm.main.dto.request.OrganizationRequest;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.*;
import com.crm.main.service.producer.OrgUserRoleChangedProducer;
import com.crm.main.service.wrapper.FileClientWrapper;
import com.crm.main.service.wrapper.RoleClientWrapper;
import com.crm.sharedlib.core.dto.request.CreateRoleRequest;
import com.crm.sharedlib.core.dto.request.ResourceWithActionsRequest;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import com.crm.sharedlib.core.enums.Action;
import com.crm.sharedlib.core.enums.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationCreatorService {

    private final OrganizationRoleService organizationRoleService;
    private final OrganizationService organizationService;
    private final OrganizationUserService organizationUserService;
    private final OrganizationRoleUserService organizationRoleUserService;
    private final OrganizationFileService fileService;

    private final RoleClientWrapper roleClientWrapper;
    private final FileClientWrapper fileClientWrapper;

    private final OrgUserRoleChangedProducer roleChangedProducer;

    @Value("${app.roles.default-admin-name}")
    private String adminRoleName;
    @Value("${app.roles.default-member-name}")
    private String memberRoleName;
    @Value("${app.files.default-root-dir-prefix-name}")
    private String rootDirPrefixName;

    @Transactional
    public Organization createOrganization(
            OrganizationRequest request,
            Long userId
    ) {
        Organization organization = organizationService.createOrganization(request);

        OrganizationRole role = createAdminRoleForOrganization(organization);

        createMemberRoleForOrganization(organization);

        OrganizationUser userOfOrganization =
                organizationUserService.createUserOfOrganization(organization, userId);

        OrganizationRoleUser roleForOrganizationUser =
                organizationRoleUserService.createRoleForOrganizationUser(role, userOfOrganization);

        createRootOrganizationDirectory(organization);

        roleChangedProducer.sendOrgUserRoleChanged(organization, userOfOrganization);

        return organization;
    }

    private void createRootOrganizationDirectory(Organization organization) {
        UUID fileId =
                fileClientWrapper.createDefaultDirectory(organization.getName() + " " + rootDirPrefixName);

        fileService.createRootOrganizationFile(organization, fileId);
    }

    private OrganizationRole createAdminRoleForOrganization(Organization organization) {
        RoleResponse roleResponse =
                createRoleWithOneAccessControl(
                        adminRoleName, Resource.ALL, Collections.singletonList(Action.ALL)
                );

        return organizationRoleService
                .createAdminRoleForOrganization(organization, roleResponse.getId());
    }

    private void createMemberRoleForOrganization(Organization organization) {
        RoleResponse roleResponse =
                createRoleWithOneAccessControl(
                        memberRoleName, Resource.ALL, Collections.singletonList(Action.READ)
                );

        organizationRoleService
                .createMemberRoleForOrganization(organization, roleResponse.getId());
    }

    private RoleResponse createRoleWithOneAccessControl(String name, Resource resource, List<Action> actions) {
        CreateRoleRequest roleRequest = new CreateRoleRequest();

        roleRequest.setName(name);
        roleRequest.setIsDeletable(false);

        ResourceWithActionsRequest actionsRequest = new ResourceWithActionsRequest();
        actionsRequest.setResource(resource);
        actionsRequest.setActions(actions);

        roleRequest.setResources(Collections.singletonList(actionsRequest));

        return roleClientWrapper.createRole(roleRequest, false);
    }

}
