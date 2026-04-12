package com.crm.main.persistance.repository;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Long> {

    List<TaskPriority> findByOrganization(Organization organization);

}
