package com.ipaas.tasks.application.mapper;

import com.ipaas.tasks.application.dto.TaskRequest;
import com.ipaas.tasks.application.dto.TaskResponse;
import com.ipaas.tasks.domain.model.TaskEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface TaskMapper {

    TaskEntity toEntity(TaskRequest req);

    TaskResponse toResponse(TaskEntity entity);

    List<TaskResponse> toResponseList(List<TaskEntity> list);
}