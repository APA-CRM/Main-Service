package com.crm.main.config.properties;

import com.crm.main.enums.TaskStatusType;
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

    private PrioritiesSection priorities;

    private StatusSection statuses;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrioritiesSection {
        private Map<String, String> defaultValues;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusSection {
        private Map<String, StatusConfig> defaultValues;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusConfig {
        private String color;

        private TaskStatusType type;
    }
}
