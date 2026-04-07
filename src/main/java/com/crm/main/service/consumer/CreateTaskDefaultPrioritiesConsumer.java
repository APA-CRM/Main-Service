package com.crm.main.service.consumer;

import com.crm.main.config.properties.TasksConfigProperties;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.TaskPriority;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.TaskPriorityService;
import com.crm.sharedlib.messaging.dto.amqp.OrgCreatedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.CREATE_DEFAULT_TASK_PRIORITIES_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskDefaultPrioritiesConsumer {

    private final OrganizationService organizationService;
    private final TaskPriorityService taskPriorityService;

    private final TasksConfigProperties tasksConfigProperties;

    @RabbitListener(queues = CREATE_DEFAULT_TASK_PRIORITIES_QUEUE)
    public void createDefaultPrioritiesConsumer(OrgCreatedMessage message) {
        Long organizationId = message.getOrganizationId();

        try {
            log.debug("Start to create default task priorities for organization {}", organizationId);

            Organization organization =
                    organizationService.getOrganizationOrThrowException(organizationId);

            Map<String, String> defaultValues = tasksConfigProperties.getPriorities().getDefaultValues();

            ArrayList<TaskPriority> priorities = new ArrayList<>(defaultValues.size());

            for (Map.Entry<String, String> entry : defaultValues.entrySet()) {
                TaskPriority taskPriority = new TaskPriority();

                taskPriority.setName(entry.getKey());
                taskPriority.setColor(entry.getValue());
                taskPriority.setOrganization(organization);

                priorities.add(taskPriority);
            }

            taskPriorityService.createTaskProprieties(priorities);

            log.debug("Default task priorities for organization {} has been created", organizationId);
        } catch (Exception e) {
            log.error("Error has occurred while creating default task priorities for organization {}", organizationId, e);
            throw new AmqpRejectAndDontRequeueException(e);
        }


    }

}
