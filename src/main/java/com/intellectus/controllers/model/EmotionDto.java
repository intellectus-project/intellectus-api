package com.intellectus.controllers.model;

import com.intellectus.model.constants.Emotion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmotionDto {
    private Emotion emotion;
    private Double percentage;
}
