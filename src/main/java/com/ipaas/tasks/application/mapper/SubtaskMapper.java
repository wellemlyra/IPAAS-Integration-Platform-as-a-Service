package com.ipaas.tasks.application.mapper;

import com.ipaas.tasks.application.dto.SubtaskRequest;
import com.ipaas.tasks.application.dto.SubtaskResponse;
import com.ipaas.tasks.domain.model.SubtaskEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface SubtaskMapper {

    SubtaskEntity toEntity(SubtaskRequest req);

    SubtaskResponse toResponse(SubtaskEntity entity);

    List<SubtaskResponse> toResponseList(List<SubtaskEntity> list);
}