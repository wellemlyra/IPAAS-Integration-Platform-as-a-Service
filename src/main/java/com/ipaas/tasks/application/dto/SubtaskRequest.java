package com.ipaas.tasks.application.dto;

import com.ipaas.tasks.domain.model.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubtaskRequest {
    private String title;
    private Status status;
    private UUID taskId; // referência à Task principal
}
