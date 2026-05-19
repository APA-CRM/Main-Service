package com.crm.main.persistance.repository;

import com.crm.main.persistance.entity.Task;
import com.crm.main.persistance.entity.TaskPriority;
import com.crm.main.persistance.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    boolean existsByStatus(TaskStatus status);

    boolean existsByPriority(TaskPriority priority);

}
