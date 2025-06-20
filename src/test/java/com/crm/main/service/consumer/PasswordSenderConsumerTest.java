package com.crm.main.service.consumer;

import com.crm.main.controller.BaseIntegrationTest;
import com.crm.sharedlib.dto.amqp.SendPasswordByEmailEvent;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class PasswordSenderConsumerIntegrationTest extends BaseIntegrationTest {

    @MockitoBean
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordSenderConsumer consumer;

    @Test
    @DisplayName("Verification of the method when generating a password")
    void emailServiceTest() {
        SendPasswordByEmailEvent message = new SendPasswordByEmailEvent();
        message.setEmail("test@local");
        message.setPassword("123456");

        Mockito.when(javaMailSender.createMimeMessage())
                .thenReturn(Mockito.mock(MimeMessage.class));

        consumer.sendPassword(message);

    }

}
