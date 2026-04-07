package com.crm.main.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
@ConfigurationProperties(prefix = "app.tasks")
public class TasksConfigProperties {

    private Section priorities;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Section {
        private Map<String, String> defaultValues;
    }
}
