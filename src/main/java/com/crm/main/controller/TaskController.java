package com.crm.main.controller;

import com.crm.main.annotations.OrganizationId;
import com.crm.main.annotations.RequiresOrganizationMembership;
import com.crm.main.annotations.UserId;
import com.crm.main.dto.request.TaskFilterRequest;
import com.crm.main.dto.request.TaskRequest;
import com.crm.main.dto.response.TaskResponse;
import com.crm.main.facade.TaskFacade;
import com.crm.sharedlib.rbac.annotation.RequiresPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;
import static com.crm.sharedlib.core.enums.Action.*;
import static com.crm.sharedlib.core.enums.Resource.TASKS;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class TaskController {

    private final TaskFacade facade;

    @GetMapping("/tasks/{taskId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = TASKS, action = READ)
    public TaskResponse getTask(
            @PathVariable("taskId") UUID taskId,
            @OrganizationId Long organizationId,
            @UserId @RequestHeader(USER_ID_HEADER_NAME) Long userId
    ) {
        return facade.getTask(taskId);
    }

    @GetMapping("/tasks/filter")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = TASKS, action = READ)
    public Page<TaskResponse> filterTasks(
            @OrganizationId Long organizationId,
            @UserId @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @ModelAttribute TaskFilterRequest request
    ) {
        return facade.filterTasks(request);
    }

    @GetMapping("/{organizationId}/tasks/")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = TASKS, action = CREATE)
    public TaskResponse createTask(
            @OrganizationId Long organizationId,
            @UserId @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody TaskRequest request
    ) {
        return facade.createTask(organizationId, request, userId);
    }

    @PutMapping("/tasks/{taskId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = TASKS, action = UPDATE)
    public TaskResponse updateTask(
            @PathVariable("taskId") UUID taskId,
            @OrganizationId Long organizationId,
            @UserId @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody TaskRequest request
    ) {
        return facade.updateTask(taskId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/tasks/{taskId}")
    @RequiresOrganizationMembership
    @RequiresPermission(resource = TASKS, action = READ)
    public void deleteTask(
            @PathVariable("taskId") UUID taskId,
            @OrganizationId Long organizationId,
            @UserId @RequestHeader(USER_ID_HEADER_NAME) Long userId
    ) {
        facade.deleteTask(taskId);
    }

}
