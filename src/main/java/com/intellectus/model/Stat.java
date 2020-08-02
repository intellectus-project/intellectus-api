package com.intellectus.model;

import com.intellectus.model.constants.Emotion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
@Table(name = "stats")
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    private double sadness;
    private double happiness;
    private double fear;
    private double neutrality;
    private double anger;

    @JoinColumn(name = "ID_CALL")
    @ManyToOne
    private Call call;

    private String speakerType;

    public Stat() {}

    public Stat(double sadness, double happiness, double fear, double neutrality, double anger, Call call, String speakerType){
        this.sadness = sadness;
        this.happiness = happiness;
        this.fear = fear;
        this.neutrality = neutrality;
        this.anger = anger;
        this.call = call;
        this.speakerType = speakerType;
    }

    public String getPrimaryEmotion() {
        double maxEmotion = Collections.max(Arrays.asList(this.sadness, this.happiness, this.fear, this.neutrality, this.anger));
        if (maxEmotion == sadness){
            return Emotion.EMOTION_SADNESS.getEmotion();
        } else if (maxEmotion == happiness){
            return Emotion.EMOTION_HAPPINESS.getEmotion();
        } else if (maxEmotion == fear){
            return Emotion.EMOTION_FEAR.getEmotion();
        } else if (maxEmotion == neutrality){
            return Emotion.EMOTION_NEUTRALITY.getEmotion();
        } else if (maxEmotion == anger) {
            return Emotion.EMOTION_ANGER.getEmotion();
        }
        return "";
    }
}
