package com.crm.main.service.producer;

import com.crm.main.persistance.entity.Organization;
import com.crm.sharedlib.messaging.dto.amqp.OrgCreatedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.MAIN_SERVICE_EXCHANGER_NAME;
import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.ORGANIZATION_CREATED_ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class OrganizationCreatedProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrganizationCreatedEvent(Organization organization) {

        OrgCreatedMessage message = new OrgCreatedMessage(organization.getId(), organization.getName());

        rabbitTemplate.convertAndSend(MAIN_SERVICE_EXCHANGER_NAME, ORGANIZATION_CREATED_ROUTING_KEY, message);
    }

}
