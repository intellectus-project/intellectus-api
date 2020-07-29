package com.intellectus.model.constants;

public enum SpeakerType {
    SPEAKER_TYPE_OPERATOR("SPEAKER_TYPE_OPERATOR"),
    SPEAKER_TYPE_CONSULTANT("SPEAKER_TYPE_CONSULTANT");

    private String speakerType;

    private SpeakerType(String speakerType) {
        this.speakerType = speakerType;
    }

    public String getSpeakerType() {
        return this.speakerType;
    }
}
