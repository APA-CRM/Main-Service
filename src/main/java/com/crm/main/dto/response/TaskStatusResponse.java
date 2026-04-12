package com.crm.main.dto.response;

import com.crm.main.enums.TaskStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusResponse {

    private Long id;

    private String name;

    private String color;

    private TaskStatusType type;

    private Instant createdAt;

    private Instant updatedAt;

}
