package com.crm.main.controller;

import com.crm.main.annotations.OrganizationId;
import com.crm.main.annotations.RequiresOrganizationMembership;
import com.crm.main.annotations.UserId;
import com.crm.main.dto.request.TaskStatusRequest;
import com.crm.main.dto.response.TaskStatusResponse;
import com.crm.main.facade.TaskStatusFacade;
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
public class TaskStatusController {

    private final TaskStatusFacade facade;

    @GetMapping("/{organizationId}/tasks/statuses")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.TASK_STATUSES, action = Action.READ)
    public List<TaskStatusResponse> getTaskPrioritiesByOrganization(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long userId
    ) {
        return facade.getTaskStatusesByOrganization(organizationId);
    }

    @GetMapping("/{organizationId}/tasks/statuses/{taskStatusId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.TASK_STATUSES, action = Action.READ)
    public TaskStatusResponse getTaskPriority(
            @PathVariable("taskStatusId") Long taskStatusId,
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long userId
    ) {
        return facade.getTaskStatus(taskStatusId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{organizationId}/tasks/statuses")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.TASK_STATUSES, action = Action.CREATE)
    public TaskStatusResponse createTaskPriority(
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long userId,
            @Valid @RequestBody TaskStatusRequest request
    ) {
        return facade.createTaskStatus(organizationId, request);
    }

    @PutMapping("/{organizationId}/tasks/statuses/{taskStatusId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.TASK_STATUSES, action = Action.UPDATE)
    public TaskStatusResponse updateTaskPriority(
            @PathVariable("taskStatusId") Long taskStatusId,
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long userId,
            @Valid @RequestBody TaskStatusRequest request
    ) {
        return facade.updateTaskStatus(taskStatusId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{organizationId}/tasks/statuses/{taskStatusId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = Resource.TASK_STATUSES, action = Action.DELETE)
    public void deleteTaskPriority(
            @PathVariable("taskStatusId") Long taskStatusId,
            @OrganizationId @PathVariable("organizationId") Long organizationId,
            @RequestHeader(USER_ID_HEADER_NAME) @UserId Long userId
    ) {
        facade.deleteTaskStatus(taskStatusId);
    }

}
