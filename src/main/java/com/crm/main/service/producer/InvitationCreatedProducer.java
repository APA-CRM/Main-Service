package com.crm.main.service.producer;

import com.crm.main.persistance.entity.OrganizationInvitation;
import com.crm.sharedlib.messaging.dto.amqp.SendInvitationOfOrganizationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.MAIN_SERVICE_EXCHANGER_NAME;
import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.ORGANIZATION_INVITATION_CREATED_ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class InvitationCreatedProducer {

    private final RabbitTemplate rabbitTemplate;

    public void notifyUserAboutInvitationOfOrganization(OrganizationInvitation invitation, String email) {

        SendInvitationOfOrganizationMessage message = new SendInvitationOfOrganizationMessage();
        message.setEmail(email);
        message.setInvitationId(invitation.getId());
        message.setOrganizationName(invitation.getOrganization().getName());

        rabbitTemplate.convertAndSend(MAIN_SERVICE_EXCHANGER_NAME, ORGANIZATION_INVITATION_CREATED_ROUTING_KEY, message);
    }

}
