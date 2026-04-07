package com.crm.main.service;

import com.crm.main.persistance.entity.TaskPriority;
import com.crm.main.persistance.repository.TaskPriorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TaskPriorityService {

    private final TaskPriorityRepository repository;

    @Transactional
    public void createTaskProprieties(Collection<TaskPriority> taskPriorities) {
        repository.saveAll(taskPriorities);
    }

}
