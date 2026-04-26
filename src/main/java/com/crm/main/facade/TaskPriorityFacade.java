package com.crm.main.facade;

import com.crm.main.dto.request.TaskPriorityRequest;
import com.crm.main.dto.response.TaskPriorityResponse;
import com.crm.main.mapper.TaskPriorityMapper;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.TaskPriority;
import com.crm.main.service.OrganizationService;
import com.crm.main.service.TaskPriorityService;
import com.crm.sharedlib.core.annotations.Facade;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class TaskPriorityFacade {

    private final TaskPriorityService priorityService;
    private final OrganizationService organizationService;

    private final TaskPriorityMapper priorityMapper;

    public List<TaskPriorityResponse> getTaskPrioritiesByOrganization(Long organizationId) {
        Organization organization = organizationService.getOrganizationOrThrowException(organizationId);

        List<TaskPriority> priorities = priorityService.getTaskPrioritiesByOrganization(organization);

        return priorities.stream()
                .map(priorityMapper::toResponse)
                .toList();
    }

    public TaskPriorityResponse getTaskPriority(Long taskPriorityId) {
        TaskPriority taskPriority = priorityService.getTaskPriorityOrThrowException(taskPriorityId);

        return priorityMapper.toResponse(taskPriority);
    }

    public TaskPriorityResponse createTaskPriority(Long organizationId, TaskPriorityRequest request) {
        Organization organization = organizationService.getOrganizationOrThrowException(organizationId);

        TaskPriority taskPriority = priorityService.createTaskPriority(request, organization);

        return priorityMapper.toResponse(taskPriority);
    }

    public TaskPriorityResponse updateTaskPriority(Long taskPriorityId, TaskPriorityRequest request) {
        TaskPriority taskPriority = priorityService.updateTaskPriority(taskPriorityId, request);

        return priorityMapper.toResponse(taskPriority);
    }

    public void deleteTaskPriority(Long taskPriorityId) {
        priorityService.deleteTaskPriority(taskPriorityId);
    }

}
