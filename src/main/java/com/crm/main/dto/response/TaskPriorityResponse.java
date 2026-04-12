package com.crm.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskPriorityResponse {

    private Long id;

    private String name;

    private String color;

    private Instant createdAt;

    private Instant updatedAt;

}
