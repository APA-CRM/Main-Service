package com.crm.main.mapper;

import com.crm.main.dto.response.TaskStatusResponse;
import com.crm.main.persistance.entity.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskStatusMapper {

    TaskStatusResponse toResponse(TaskStatus taskStatus);

}
