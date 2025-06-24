package com.crm.main.service.consumer;

import com.crm.sharedlib.dto.amqp.SendInvitationOfOrganizationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.crm.sharedlib.consts.CrmConstants.SEND_INVITATION_OF_ORGANIZATION;

@Service
@RequiredArgsConstructor
public class SendInvitationOfOrganizationConsumer {

    @RabbitListener(queues = SEND_INVITATION_OF_ORGANIZATION)
    public void sendInvitationOfOrganizationToUser(SendInvitationOfOrganizationEvent event) {

    }

}
