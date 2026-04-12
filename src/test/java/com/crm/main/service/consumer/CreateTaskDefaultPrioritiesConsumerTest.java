package com.crm.main.service.consumer;

import com.crm.main.BaseIntegrationTestWithRabbitMQ;
import com.crm.main.config.properties.TasksConfigProperties;
import com.crm.main.persistance.entity.TaskPriority;
import com.crm.main.persistance.repository.TaskPriorityRepository;
import com.crm.sharedlib.messaging.dto.amqp.OrgCreatedMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.MAIN_SERVICE_EXCHANGER_NAME;
import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.ORGANIZATION_CREATED_ROUTING_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Sql(scripts = "classpath:sql/insertTestOrganizations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:sql/deleteTestTaskPriorities.sql",
        "classpath:sql/deleteTestOrganization.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CreateTaskDefaultPrioritiesConsumerTest extends BaseIntegrationTestWithRabbitMQ {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TasksConfigProperties tasksConfigProperties;

    @MockitoSpyBean
    private TaskPriorityRepository priorityRepository;

    @MockitoBean
    private CreateTaskDefaultStatusesConsumer statusesConsumer;

    @Test
    @DisplayName("Create default priorities consumer expected success")
    public void createDefaultPrioritiesConsumerExpectedSuccess() {

        OrgCreatedMessage message = new OrgCreatedMessage(100L, "NewPoshta");

        rabbitTemplate.convertAndSend(MAIN_SERVICE_EXCHANGER_NAME, ORGANIZATION_CREATED_ROUTING_KEY, message);

        await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        Mockito.verify(priorityRepository, Mockito.atLeastOnce())
                                .saveAll(Mockito.any())
                );

        List<TaskPriority> priorities = priorityRepository.findAll();

        Map<String, String> defaultValues = tasksConfigProperties.getPriorities().getDefaultValues();

        assertThat(priorities)
                .hasSize(defaultValues.size())
                .allMatch(taskPriority -> defaultValues.containsKey(taskPriority.getName())
                        && defaultValues.get(taskPriority.getName()).equals(taskPriority.getColor()))
                .map(taskPriority -> taskPriority.getOrganization().getId())
                .allMatch(organizationId -> organizationId.equals(message.getOrganizationId()));
    }

}