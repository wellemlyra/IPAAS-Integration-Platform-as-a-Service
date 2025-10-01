package com.ipaas.tasks.application.dto;

import com.ipaas.tasks.domain.model.Status;
import lombok.*;

import java.util.UUID;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubtaskResponse {
    private UUID id;
    private String title;
    private Status status;
    private OffsetDateTime completedAt;
    private UUID taskId;
}
