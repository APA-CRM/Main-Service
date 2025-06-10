package com.crm.main.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final TemplateBuilder templateBuilder;

    public void sendEmail(String to, String text, String subject) {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(text, true);
        } catch (MessagingException e) {
            log.error("Something went wrong while sending email", e);
            throw new RuntimeException(e);
        }

        mailSender.send(message);

        log.info("Email successfully sent");
    }
}
