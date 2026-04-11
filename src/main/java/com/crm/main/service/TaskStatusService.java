package com.crm.main.service;

import com.crm.main.persistance.entity.TaskStatus;
import com.crm.main.persistance.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TaskStatusService {

    private final TaskStatusRepository repository;

    @Transactional
    public void createTaskStatuses(Collection<TaskStatus> taskStatuses) {
        repository.saveAll(taskStatuses);
    }

}
