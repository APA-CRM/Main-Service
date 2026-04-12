package com.crm.main.facade;

import com.crm.main.dto.request.TaskStatusRequest;
import com.crm.main.dto.response.TaskStatusResponse;
import com.crm.main.mapper.TaskStatusMapper;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.TaskStatus;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.TaskStatusService;
import com.crm.sharedlib.core.annotations.Facade;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class TaskStatusFacade {

    private final TaskStatusService taskStatusService;
    private final OrganizationService organizationService;

    private final TaskStatusMapper taskStatusMapper;

    public List<TaskStatusResponse> getTaskStatusesByOrganization(Long organizationId) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        List<TaskStatus> taskStatuses =
                taskStatusService.getTaskStatesByOrganization(organization);

        return taskStatuses.stream()
                .map(taskStatusMapper::toResponse)
                .toList();
    }

    public TaskStatusResponse getTaskStatus(Long statusId) {
        TaskStatus taskStatus = taskStatusService.getTaskStatusOrThrowException(statusId);

        return taskStatusMapper.toResponse(taskStatus);
    }

    public TaskStatusResponse createTaskStatus(Long organizationId, TaskStatusRequest request) {
        Organization organization =
                organizationService.getOrganizationOrThrowException(organizationId);

        TaskStatus taskStatus = taskStatusService.createTaskStatus(request, organization);

        return taskStatusMapper.toResponse(taskStatus);
    }

    public TaskStatusResponse updateTaskStatus(Long statusId, TaskStatusRequest request) {
        TaskStatus taskStatus = taskStatusService.updateTaskStatus(statusId, request);

        return taskStatusMapper.toResponse(taskStatus);
    }

    public void deleteTaskStatus(Long statusId) {
        taskStatusService.deleteTaskStatus(statusId);
    }

}
