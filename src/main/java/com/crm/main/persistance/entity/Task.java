package com.crm.main.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {
        @Index(name = "task_organization_id_idx", columnList = "organization_id"),
        @Index(name = "task_status_id_idx", columnList = "status_id"),
        @Index(name = "task_priority_id_idx", columnList = "priority_id"),
        @Index(name = "task_assigned_to_idx", columnList = "assigned_to"),
        @Index(name = "task_created_by_idx", columnList = "created_by"),
})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;

    private Integer estimatedTime;

    @ManyToOne(optional = false)
    private Organization organization;

    @ManyToOne(optional = false)
    private TaskStatus status;

    @ManyToOne(optional = false)
    private TaskPriority priority;

    private Long assignedTo;

    private Long createdBy;

    private Instant dueDate;

    // TODO: Provide logic of reminding about a task
    private Instant reminderAt;

    private Instant completedAt;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}
