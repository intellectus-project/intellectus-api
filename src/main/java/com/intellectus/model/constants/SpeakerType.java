package com.intellectus.model.constants;

import java.util.Arrays;
import java.util.Optional;

public enum SpeakerType {
    SPEAKER_TYPE_OPERATOR(0, "OPERATOR SPEAKER"),
    SPEAKER_TYPE_CONSULTANT(1, "CONSULTANT SPEAKER");

    private Integer id;
    private String description;

    SpeakerType(Integer id, String description){
        this.id = id;
        this.description = description;
    }

    public static Optional<SpeakerType> valueOf(int value) {
        return Arrays.stream(values())
                .filter(SpeakerTypeEnum -> SpeakerTypeEnum.id == value)
                .findFirst();
    }

}
