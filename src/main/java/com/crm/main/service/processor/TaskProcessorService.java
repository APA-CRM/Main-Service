package com.crm.main.service.processor;

import com.crm.main.dto.request.TaskRequest;
import com.crm.main.enums.UserMessage;
import com.crm.main.mapper.TaskMapper;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.Task;
import com.crm.main.persistance.entity.TaskPriority;
import com.crm.main.persistance.entity.TaskStatus;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.TaskPriorityService;
import com.crm.main.service.TaskService;
import com.crm.main.service.TaskStatusService;
import com.crm.main.service.task.updater.TaskStatusUpdater;
import com.crm.main.service.task.updater.TaskStatusUpdaterFactory;
import com.crm.sharedlib.messaging.service.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.crm.main.enums.UserMessage.TASK_HAS_BEEN_ASSIGNED;

@Service
@RequiredArgsConstructor
public class TaskProcessorService {

    private final OrganizationService organizationService;
    private final TaskStatusService taskStatusService;
    private final TaskPriorityService taskPriorityService;
    private final TaskService taskService;

    private final TaskStatusUpdaterFactory taskStatusUpdaterFactory;

    private final TaskMapper taskMapper;

    private final MessagingService messagingService;

    @Transactional
    public Task createTask(Long organizationId, TaskRequest request, Long userId) {

        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);
        TaskStatus taskStatus =
                taskStatusService.getTaskStatusOrThrowException(request.getStatusId());
        TaskPriority taskPriority =
                taskPriorityService.getTaskPriorityOrThrowException(request.getPriorityId());

        Task task = taskMapper.toTask(request);

        task.setPriority(taskPriority);
        task.setOrganization(organization);

        task.setCreatedBy(userId);

        TaskStatusUpdater statusUpdater = taskStatusUpdaterFactory.getTaskStatusUpdater(taskStatus);
        statusUpdater.update(task, taskStatus);

        Task savedTask = taskService.saveTask(task);

        if (!Objects.equals(task.getAssignedTo(), userId)) {
            sendMessageAboutAssignedTask(task, task.getAssignedTo());
        }

        return savedTask;
    }

    @Transactional
    public Task updateTask(UUID taskId, TaskRequest request, Long userId) {
        Task task = taskService.getTaskOrThrowException(taskId);

        Long previouslyAssignedTo = task.getAssignedTo();

        if (!Objects.equals(task.getStatus().getId(), request.getStatusId())) {
            TaskStatus taskStatus =
                    taskStatusService.getTaskStatusOrThrowException(request.getStatusId());
            TaskStatusUpdater statusUpdater = taskStatusUpdaterFactory.getTaskStatusUpdater(taskStatus);
            statusUpdater.update(task, taskStatus);
        }

        if (!Objects.equals(task.getPriority().getId(), request.getPriorityId())) {
            TaskPriority taskPriority =
                    taskPriorityService.getTaskPriorityOrThrowException(request.getPriorityId());
            task.setPriority(taskPriority);

        }

        task = taskMapper.updateTask(task, request);

        // Do not duplicate message about task's assigment
        if (!Objects.equals(previouslyAssignedTo, request.getAssignedTo())
                && !Objects.equals(task.getAssignedTo(), userId)) {
            sendMessageAboutAssignedTask(task, request.getAssignedTo());
        }

        return taskService.saveTask(task);
    }

    @Transactional
    public void deleteTask(UUID taskId) {
        taskService.deleteTask(taskId);
    }

    private void sendMessageAboutAssignedTask(Task task, Long userId) {
        UserMessage message = TASK_HAS_BEEN_ASSIGNED;

        messagingService.sendMessageToUser(
                userId, message.getTitle(), message.getMessageCode(),
                message.getMessage().formatted(task.getTitle()),
                Map.of("taskId", task.getId())
        );
    }

}
