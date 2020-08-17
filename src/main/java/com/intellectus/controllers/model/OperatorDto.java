package com.intellectus.controllers.model;

import com.intellectus.model.constants.Emotion;
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
    private EmotionDto primaryEmotion;
    private EmotionDto secondaryEmotion;
}
