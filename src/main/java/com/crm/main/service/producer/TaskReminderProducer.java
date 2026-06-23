package com.crm.main.service.producer;

import com.crm.main.persistance.entity.Task;
import com.crm.sharedlib.messaging.dto.amqp.TaskReminderMessage;
import com.crm.sharedlib.messaging.dto.amqp.TaskReminderMessage.TaskExtraInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.MAIN_SERVICE_EXCHANGER_NAME;
import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.TASK_REMINDER_ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class TaskReminderProducer {

    private final RabbitTemplate rabbitTemplate;

    public void remindAboutTask(Task task, List<String> emails) {
        TaskReminderMessage message = new TaskReminderMessage();
        message.setEmails(emails);
        message.setTaskId(task.getId());
        message.setTitle(task.getTitle());
        message.setCreatedAt(task.getCreatedAt());
        message.setDueDate(task.getDueDate());
        message.setOrganizationId(task.getOrganization().getId());
        message.setOrganizationName(task.getOrganization().getName());
        message.setStatus(new TaskExtraInfo(
                task.getStatus().getName(),
                task.getStatus().getColor())
        );
        message.setPriority(new TaskExtraInfo(
                task.getPriority().getName(),
                task.getPriority().getColor())
        );

        rabbitTemplate.convertAndSend(MAIN_SERVICE_EXCHANGER_NAME, TASK_REMINDER_ROUTING_KEY, message);
    }

}
