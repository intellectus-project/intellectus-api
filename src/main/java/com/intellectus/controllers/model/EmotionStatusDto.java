package com.intellectus.controllers.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EmotionStatusDto {
    StatDto status;
    String name;
    private boolean atBreak;
    private boolean breakAssignedToActualCall;
    private Boolean inCall;
    private LocalDateTime callStartTime;
}
