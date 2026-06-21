package com.crm.main.mapper;

import com.crm.main.dto.request.TaskRequest;
import com.crm.main.dto.response.TaskResponse;
import com.crm.main.persistance.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {TaskStatusMapper.class, TaskPriorityMapper.class}
)
public interface TaskMapper {

    @Mapping(target = "isReminded", ignore = true)
    @Mapping(target = "reminderJobId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "organization", ignore = true)
    Task toTask(TaskRequest request);

    @Mapping(target = "isReminded", ignore = true)
    @Mapping(target = "reminderJobId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "organization", ignore = true)
    Task updateTask(@MappingTarget Task task, TaskRequest request);

    @Mapping(target = "organizationId", source = "organization.id")
    TaskResponse toResponse(Task task);

}
