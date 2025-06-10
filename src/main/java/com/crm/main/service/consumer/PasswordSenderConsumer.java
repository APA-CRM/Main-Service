package com.crm.main.service.consumer;

import com.crm.main.service.emailService.EmailService;
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

    private EmailService emailService;

    @RabbitListener(queues = SEND_PASSWORD_QUEUE)
    public void sendPassword(SendPasswordEmail message) {
        log.info("Sending email with password to user");

        String subject = "🔐Your Generated Password!";
        emailService.sendEmail(message.getEmail(), message.getPassword(), subject);

    }

}
