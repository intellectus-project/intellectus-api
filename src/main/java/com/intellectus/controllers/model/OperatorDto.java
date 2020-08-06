package com.intellectus.controllers.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OperatorDto {
    private Long id;
    private String username;
    private Boolean inCall;
    private LocalDateTime callStartTime;
    private String actualEmotion;
}
