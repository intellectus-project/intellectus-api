package com.intellectus.model.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Emotion {
    EMOTION_SADNESS(0, "SADNESS"),
    EMOTION_HAPPINESS(1, "HAPPINESS"),
    EMOTION_FEAR(2, "FEAR"),
    EMOTION_NEUTRALITY(3, "NEUTRALITY"),
    EMOTION_ANGER(4, "ANGER");

    private Integer id;
    private String description;

    Emotion(Integer id, String description){
        this.id = id;
        this.description = description;
    }

    public static Optional<Emotion> valueOf(int value) {
        return Arrays.stream(values())
                .filter(EmotionEnum -> EmotionEnum.id == value)
                .findFirst();
    }
}
