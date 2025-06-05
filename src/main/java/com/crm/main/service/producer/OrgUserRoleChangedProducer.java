package com.crm.main.service.producer;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationRoleUserService;
import com.crm.sharedlib.dto.amqp.OrgUserRoleChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.crm.main.constants.RabbitConstants.MAIN_SERVICE_EXCHANGER_NAME;
import static com.crm.main.constants.RabbitConstants.ORGANIZATION_USER_ROLE_CHANGE_ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class OrgUserRoleChangedProducer {

    private final RabbitTemplate rabbitTemplate;

    private final OrganizationRoleUserService roleUserService;

    public void sendOrgUserRoleChanged(
            Organization organization, OrganizationUser organizationUser
    ) {

        List<OrganizationRoleUser> userRoles =
                roleUserService.getOrganizationUserRoles(organizationUser);

        List<Long> rolesId = userRoles.stream()
                .map(oru -> oru.getOrganizationRole().getRoleId())
                .toList();

        OrgUserRoleChangedEvent message = OrgUserRoleChangedEvent.builder()
                .userId(organizationUser.getUserId())
                .organizationId(organization.getId())
                .rolesIs(rolesId)
                .build();

        rabbitTemplate.convertAndSend(MAIN_SERVICE_EXCHANGER_NAME, ORGANIZATION_USER_ROLE_CHANGE_ROUTING_KEY, message);

    }

}
