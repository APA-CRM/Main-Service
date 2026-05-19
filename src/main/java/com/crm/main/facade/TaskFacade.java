package com.crm.main.facade;

import com.crm.main.dto.request.TaskFilterRequest;
import com.crm.main.dto.request.TaskRequest;
import com.crm.main.dto.response.TaskResponse;
import com.crm.main.filter.TaskFilter;
import com.crm.main.mapper.TaskMapper;
import com.crm.main.persistance.entity.Task;
import com.crm.main.service.TaskService;
import com.crm.main.service.processor.TaskProcessorService;
import com.crm.sharedlib.core.annotations.Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.UUID;

import static com.crm.main.constants.AppConstants.ORGANIZATION_ID_FILTER_KEY_NAME;

@Facade
@RequiredArgsConstructor
public class TaskFacade {

    private final TaskProcessorService processorService;
    private final TaskService taskService;

    private final TaskFilter taskFilter;

    private final TaskMapper taskMapper;

    public TaskResponse getTask(UUID taskId) {
        Task task = taskService.getTaskOrThrowException(taskId);

        return taskMapper.toResponse(task);
    }

    public PagedModel<TaskResponse> filterTasks(Long organizationId, TaskFilterRequest request) {
        request.addAdditionalField(ORGANIZATION_ID_FILTER_KEY_NAME, organizationId);

        PageImpl<Task> tasks = taskFilter.filter(request);

        return new PagedModel<>(tasks.map(taskMapper::toResponse));
    }

    public TaskResponse createTask(
            Long organizationId, TaskRequest request, Long userId
    ) {
        Task task = processorService.createTask(organizationId, request, userId);

        return taskMapper.toResponse(task);
    }

    public TaskResponse updateTask(
            UUID taskId, TaskRequest request, Long userId
    ) {
        Task task = processorService.updateTask(taskId, request, userId);

        return taskMapper.toResponse(task);
    }

    public void deleteTask(UUID taskId) {
        processorService.deleteTask(taskId);
    }

}
