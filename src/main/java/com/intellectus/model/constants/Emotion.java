package com.intellectus.model.constants;

public enum Emotion {
    EMOTION_SADNESS("EMOTION_SADNESS"),
    EMOTION_HAPPINESS("EMOTION_HAPPINESS"),
    EMOTION_FEAR("EMOTION_FEAR"),
    EMOTION_NEUTRALITY("EMOTION_NEUTRALITY"),
    EMOTION_ANGER("EMOTION_ANGER");

    private String emotion;

    private Emotion(String emotion) {
        this.emotion = emotion;
    }

    public String getEmotion() {
        return this.emotion;
    }
}
