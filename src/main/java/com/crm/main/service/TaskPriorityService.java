package com.crm.main.service;

import com.crm.main.dto.request.TaskPriorityRequest;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.TaskPriority;
import com.crm.main.persistance.repository.TaskPriorityRepository;
import com.crm.sharedlib.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskPriorityService {

    private final TaskPriorityRepository repository;

    public List<TaskPriority> getTaskPrioritiesByOrganization(Organization organization) {
        return repository.findByOrganization(organization);
    }

    public TaskPriority getTaskPriorityOrThrowException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task priority is not found"));
    }

    @Transactional
    public void createTaskPriorities(Collection<TaskPriority> taskPriorities) {
        repository.saveAll(taskPriorities);
    }

    @Transactional
    public TaskPriority createTaskPriority(TaskPriorityRequest request, Organization organization) {
        TaskPriority taskPriority = new TaskPriority();

        taskPriority.setColor(request.getColor());
        taskPriority.setName(request.getName());
        taskPriority.setOrganization(organization);

        return repository.save(taskPriority);
    }

    @Transactional
    public TaskPriority updateTaskPriority(Long id, TaskPriorityRequest request) {
        TaskPriority taskPriority = getTaskPriorityOrThrowException(id);

        taskPriority.setColor(request.getColor());
        taskPriority.setName(request.getName());

        return repository.save(taskPriority);
    }

    @Transactional
    public void deleteTaskPriority(Long id) {
        TaskPriority taskPriority = getTaskPriorityOrThrowException(id);

        repository.delete(taskPriority);
    }

}
