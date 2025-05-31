package com.crm.main.service.consumer;

import com.crm.sharedlib.dto.amqp.SendPasswordEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.crm.sharedlib.consts.CrmConstants.SEND_PASSWORD_QUEUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordSenderConsumer {

    @RabbitListener(queues = SEND_PASSWORD_QUEUE)
    public void sendPassword(SendPasswordEmail message) {

        // TODO: Send password to user by email

        log.info("Message received: Email {}, password  {}",
                message.getEmail(), message.getPassword());

    }

}
