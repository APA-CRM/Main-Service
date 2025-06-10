package com.crm.main.service.emailService;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class TemplateBuilder {

    @Autowired
    private TemplateEngine templateEngine;

    public String buildPasswordEmail(String password) {

        Context context = new Context();
        context.setVariable("password", password);

        return templateEngine.process("emailTemplate", context);

    }

}
