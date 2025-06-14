package com.crm.main.service.email;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class TemplateBuilder {

    private final TemplateEngine templateEngine;

    public String buildPasswordEmail(String password) {

        Context context = new Context();
        context.setVariable("password", password);

        return templateEngine.process("emailTemplate", context);

    }

}
