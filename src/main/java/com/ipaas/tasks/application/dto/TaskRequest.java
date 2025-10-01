package com.ipaas.tasks.application.dto;

import com.ipaas.tasks.domain.model.Priority;
import com.ipaas.tasks.domain.model.Status;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private Status status;
    @NotNull
    private Priority priority;
    private OffsetDateTime dueDate;
    private UUID assigneeId;
}
