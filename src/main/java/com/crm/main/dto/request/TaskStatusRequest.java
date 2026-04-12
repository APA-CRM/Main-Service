package com.crm.main.dto.request;

import com.crm.main.enums.TaskStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusRequest {

    @NotBlank(message = "Task status name cannot be blank")
    private String name;

    @NotBlank(message = "Task status color cannot be blank")
    private String color;

    @NotNull(message = "Task status type cannot be null")
    private TaskStatusType type;

}
