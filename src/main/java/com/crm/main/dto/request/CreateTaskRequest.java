package com.crm.main.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

    private Integer estimatedTime;

    @NotNull(message = "Status ID cannot be null")
    private Long statusId;

    @NotNull(message = "Priority ID cannot be null")
    private Long priorityId;

    // TODO: Verify existence of the user
    private Long assignedTo;

    private Instant dueDate;

    private Instant reminderAt;

}
