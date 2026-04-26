package com.crm.main.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskPriorityRequest {

    @NotBlank(message = "Task priority name cannot be blank")
    private String name;

    @NotBlank(message = "Task priority color cannot be blank")
    private String color;

}
