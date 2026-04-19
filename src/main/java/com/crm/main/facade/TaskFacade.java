package com.crm.main.facade;

import com.crm.main.dto.request.TaskRequest;
import com.crm.main.dto.response.TaskResponse;
import com.crm.main.mapper.TaskMapper;
import com.crm.main.persistance.entity.Task;
import com.crm.main.service.TaskService;
import com.crm.main.service.processor.TaskProcessorService;
import com.crm.sharedlib.core.annotations.Facade;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Facade
@RequiredArgsConstructor
public class TaskFacade {

    private final TaskProcessorService processorService;
    private final TaskService taskService;

    private final TaskMapper taskMapper;

    public TaskResponse getTask(UUID taskId) {
        Task task = taskService.getTaskOrThrowException(taskId);

        return taskMapper.toResponse(task);
    }

    public TaskResponse createTask(
            Long organizationId, TaskRequest request, Long userId
    ) {
        Task task = processorService.createTask(organizationId, request, userId);

        return taskMapper.toResponse(task);
    }

    public TaskResponse updateTask(
            UUID taskId, TaskRequest request
    ) {
        Task task = processorService.updateTask(taskId, request);

        return taskMapper.toResponse(task);
    }

    public void deleteTask(UUID taskId) {
        processorService.deleteTask(taskId);
    }

}
