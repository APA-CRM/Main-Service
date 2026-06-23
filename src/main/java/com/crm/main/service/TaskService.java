package com.crm.main.service;

import com.crm.main.persistance.entity.Task;
import com.crm.main.persistance.entity.TaskPriority;
import com.crm.main.persistance.entity.TaskStatus;
import com.crm.main.persistance.repository.TaskRepository;
import com.crm.sharedlib.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public Task getTaskOrThrowException(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task is not found"));
    }

    @Transactional
    public Task saveTask(Task task) {
        return repository.save(task);
    }

    @Transactional
    public void deleteTask(Task task) {
        repository.delete(task);
    }

    public boolean taskExistsWithStatus(TaskStatus status) {
        return repository.existsByStatus(status);
    }

    public boolean taskExistsWithPriority(TaskPriority priority) {
        return repository.existsByPriority(priority);
    }

}
