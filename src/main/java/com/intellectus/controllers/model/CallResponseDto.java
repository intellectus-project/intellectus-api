package com.intellectus.controllers.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallResponseDto {
    private Long callId;

    public CallResponseDto(Long callId) { this.callId = callId; }
}