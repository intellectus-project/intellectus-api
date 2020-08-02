package com.intellectus.controllers.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OperatorDto {
    private Long id;
    private String username;
    private Boolean inCall;
    private LocalDateTime callStartTime;
    private String actualEmotion;

    public OperatorDto(Long id, String username, LocalDateTime callStartTime, String actualEmotion) {
        this.id = id;
        this.username = username;
        this.inCall = callStartTime != null;
        this.callStartTime = callStartTime;
        this.actualEmotion = actualEmotion;
    }
}
