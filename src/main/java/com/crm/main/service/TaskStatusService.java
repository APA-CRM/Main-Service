package com.crm.main.service;

import com.crm.main.dto.request.TaskStatusRequest;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.TaskStatus;
import com.crm.main.persistance.repository.TaskStatusRepository;
import com.crm.sharedlib.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusService {

    private final TaskStatusRepository repository;

    public List<TaskStatus> getTaskStatesByOrganization(Organization organization) {
        return repository.findByOrganization(organization);
    }

    public TaskStatus getTaskStatusOrThrowException(Long statusId) {
        return repository.findById(statusId)
                .orElseThrow(() -> new NotFoundException("Task status is not found"));
    }

    @Transactional
    public void createTaskStatuses(Collection<TaskStatus> taskStatuses) {
        repository.saveAll(taskStatuses);
    }

    @Transactional
    public TaskStatus createTaskStatus(TaskStatusRequest request, Organization organization) {
        TaskStatus taskStatus = new TaskStatus();

        taskStatus.setName(request.getName());
        taskStatus.setColor(request.getColor());
        taskStatus.setType(request.getType());
        taskStatus.setOrganization(organization);

        return repository.save(taskStatus);
    }

    @Transactional
    public TaskStatus updateTaskStatus(Long statusId, TaskStatusRequest request) {
        TaskStatus taskStatus = getTaskStatusOrThrowException(statusId);

        taskStatus.setName(request.getName());
        taskStatus.setColor(request.getColor());
        taskStatus.setType(request.getType());

        return repository.save(taskStatus);
    }

    @Transactional
    public void deleteTaskStatus(Long statusId) {
        TaskStatus taskStatus = getTaskStatusOrThrowException(statusId);

        repository.delete(taskStatus);
    }

}
