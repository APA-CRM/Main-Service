package com.crm.main.controller;

import com.crm.main.annotations.OrganizationId;
import com.crm.main.annotations.RequiresOrganizationMembership;
import com.crm.main.annotations.UserId;
import com.crm.main.dto.request.TaskPriorityRequest;
import com.crm.main.dto.response.TaskPriorityResponse;
import com.crm.main.facade.TaskPriorityFacade;
import com.crm.sharedlib.core.enums.Action;
import com.crm.sharedlib.core.enums.Resource;
import com.crm.sharedlib.rbac.annotation.RequiresPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class TaskPriorityController {

    private final TaskPriorityFacade facade;

    @GetMapping("/{organizationId}/tasks/priorities")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.TASK_PRIORITIES, action = Action.READ)
    public List<TaskPriorityResponse> getTaskPrioritiesByOrganization(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long userId
    ) {
        return facade.getTaskPrioritiesByOrganization(organizationId);
    }

    @GetMapping("/{organizationId}/tasks/priorities/{taskPriorityId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.TASK_PRIORITIES, action = Action.READ)
    public TaskPriorityResponse getTaskPriority(
            @PathVariable("taskPriorityId") Long taskPriorityId,
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long userId
    ) {
        return facade.getTaskPriority(taskPriorityId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{organizationId}/tasks/priorities")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.TASK_PRIORITIES, action = Action.CREATE)
    public TaskPriorityResponse createTaskPriority(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long userId,
            @Valid @RequestBody TaskPriorityRequest request
    ) {
        return facade.createTaskPriority(organizationId, request);
    }

    @PutMapping("/{organizationId}/tasks/priorities/{taskPriorityId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.TASK_PRIORITIES, action = Action.UPDATE)
    public TaskPriorityResponse updateTaskPriority(
            @PathVariable("taskPriorityId") Long taskPriorityId,
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long userId,
            @Valid @RequestBody TaskPriorityRequest request
    ) {
        return facade.updateTaskPriority(taskPriorityId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{organizationId}/tasks/priorities/{taskPriorityId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.TASK_PRIORITIES, action = Action.DELETE)
    public void deleteTaskPriority(
            @PathVariable("taskPriorityId") Long taskPriorityId,
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long userId
    ) {
        facade.deleteTaskPriority(taskPriorityId);
    }

}
