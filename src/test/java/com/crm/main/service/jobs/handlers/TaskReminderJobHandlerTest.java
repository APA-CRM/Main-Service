package com.crm.main.service.jobs.handlers;

import com.crm.main.BaseIntegrationTest;
import com.crm.main.feign.AuthClient;
import com.crm.main.persistance.entity.Task;
import com.crm.main.persistance.repository.TaskRepository;
import com.crm.main.service.jobs.requests.TaskReminderJobRequest;
import com.crm.sharedlib.core.dto.response.UserResponse;
import org.jobrunr.scheduling.BackgroundJobRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = {
        "classpath:sql/insertTestOrganizations.sql",
        "classpath:sql/insertTestTaskPriorities.sql",
        "classpath:sql/insertTestTaskStatuses.sql",
        "classpath:sql/insertTestTasks.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:sql/deleteTestTasks.sql",
        "classpath:sql/deleteTestTaskPriorities.sql",
        "classpath:sql/deleteTestTaskStatuses.sql",
        "classpath:sql/deleteTestOrganization.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TaskReminderJobHandlerTest extends BaseIntegrationTest {

    @MockitoBean
    private RabbitTemplate rabbitTemplate;
    @MockitoBean
    private AuthClient authClient;

    @MockitoSpyBean
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Remind about a task expected success sending a message to the rabbit's queue")
    public void remindAboutTaskExpectedSuccess() {
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail("email");

        Mockito.when(authClient.getUserById(Mockito.anyLong()))
                .thenReturn(userResponse);

        UUID taskId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        BackgroundJobRequest.enqueue(new TaskReminderJobRequest(taskId));

        await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    Task task = taskRepository.findById(taskId).orElseThrow();

                    assertTrue(task.getIsReminded());
                    assertNull(task.getReminderAt());
                    assertNull(task.getReminderJobId());
                });

        Optional<Task> taskOptional = taskRepository.findById(taskId);

        assertTrue(taskOptional.isPresent(), "Task is not found, expected to be found");

        Task task = taskOptional.get();

        assertNull(task.getReminderAt(), "Reminder at time is is not null, expected null value");
        assertNull(task.getReminderJobId(), "Reminder Job ID is not null, expected null value");
        assertTrue(task.getIsReminded(), "Is reminded is false, expected true");
    }

}