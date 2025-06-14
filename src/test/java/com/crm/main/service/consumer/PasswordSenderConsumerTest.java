package com.crm.main.service.consumer;

import com.crm.main.service.email.EmailService;
import com.crm.main.service.email.TemplateBuilder;
import com.crm.sharedlib.dto.amqp.SendPasswordEmail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(MockitoExtension.class)

@SpringBootTest
@ActiveProfiles("test")
class PasswordSenderConsumerIntegrationTest {
    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private TemplateBuilder templateBuilder;

    @Autowired
    private PasswordSenderConsumer consumer;

    @Test
    @DisplayName("Verification of the method when generating a password")
    void emailServiceTest() {
        SendPasswordEmail message = new SendPasswordEmail();
        message.setEmail("test@local");
        message.setPassword("123456");

        Mockito.when(templateBuilder.buildPasswordEmail(message.getPassword())).thenReturn("emailTemplate");

        consumer.sendPassword(message);

        Mockito.verify(emailService, Mockito.times(1)).sendEmail(message.getEmail(), "emailTemplate", "🔐Your Generated Password!");
    }
}
