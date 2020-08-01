package com.intellectus.controllers.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CallDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String emotion;
    private StatDto consultant;
    private StatDto operator;
}