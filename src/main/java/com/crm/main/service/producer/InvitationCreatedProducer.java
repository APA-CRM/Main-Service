package com.crm.main.service.producer;

import com.crm.main.persistance.entity.OrganizationInvitation;
import com.crm.sharedlib.core.dto.amqp.SendInvitationOfOrganizationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.crm.main.constants.RabbitConstants.MAIN_SERVICE_EXCHANGER_NAME;
import static com.crm.main.constants.RabbitConstants.ORGANIZATION_INVITATION_CREATED;

@Service
@RequiredArgsConstructor
public class InvitationCreatedProducer {

    private final RabbitTemplate rabbitTemplate;

    public void notifyUserAboutInvitationOfOrganization(OrganizationInvitation invitation, String email) {

        SendInvitationOfOrganizationEvent event = new SendInvitationOfOrganizationEvent();
        event.setEmail(email);
        event.setInvitationId(invitation.getId());
        event.setOrganizationName(invitation.getOrganization().getName());

        rabbitTemplate.convertAndSend(MAIN_SERVICE_EXCHANGER_NAME, ORGANIZATION_INVITATION_CREATED, event);
    }

}
