package com.ipaas.tasks.application.dto;

import com.ipaas.tasks.domain.model.Status;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubtaskStatusUpdateRequest {
    @NotNull
    private Status status;
}
