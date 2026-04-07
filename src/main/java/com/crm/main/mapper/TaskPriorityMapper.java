package com.crm.main.mapper;

import com.crm.main.dto.response.TaskPriorityResponse;
import com.crm.main.persistance.entity.TaskPriority;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskPriorityMapper {

    TaskPriorityResponse toResponse(TaskPriority taskPriority);

}
