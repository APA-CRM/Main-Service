package com.crm.main.service.consumer;

import com.crm.main.config.properties.TasksConfigProperties;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.TaskStatus;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.TaskStatusService;
import com.crm.sharedlib.messaging.dto.amqp.OrgCreatedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.CREATE_DEFAULT_TASK_STATUSES_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskDefaultStatusesConsumer {

    private final OrganizationService organizationService;
    private final TaskStatusService taskStatusService;

    private final TasksConfigProperties tasksConfigProperties;

    @RabbitListener(queues = CREATE_DEFAULT_TASK_STATUSES_QUEUE)
    public void createDefaultStatusesConsumer(OrgCreatedMessage message) {
        Long organizationId = message.getOrganizationId();

        try {
            log.debug("Start to create default task statuses for organization {}", organizationId);

            Organization organization =
                    organizationService.getOrganizationOrThrowException(organizationId);

            Map<String, TasksConfigProperties.StatusConfig> defaultValues = tasksConfigProperties.getStatuses().getDefaultValues();

            ArrayList<TaskStatus> priorities = new ArrayList<>(defaultValues.size());

            for (Map.Entry<String, TasksConfigProperties.StatusConfig> entry : defaultValues.entrySet()) {
                TaskStatus taskStatus = new TaskStatus();

                taskStatus.setName(entry.getKey());
                taskStatus.setColor(entry.getValue().getColor());
                taskStatus.setType(entry.getValue().getType());
                taskStatus.setOrganization(organization);

                priorities.add(taskStatus);
            }

            taskStatusService.createTaskStatuses(priorities);

            log.debug("Default task statuses for organization {} has been created", organizationId);
        } catch (Exception e) {
            log.error("Error has occurred while creating default task statuses for organization {}", organizationId, e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

}
