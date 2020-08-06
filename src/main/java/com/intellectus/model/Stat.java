package com.intellectus.model;

import com.intellectus.model.constants.SpeakerType;
import com.intellectus.model.constants.Emotion;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
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

    @Enumerated(EnumType.STRING)
    private SpeakerType speakerType;

    public Stat() {}

    public Stat(double sadness, double happiness, double fear, double neutrality, double anger, Call call, SpeakerType speakerType){
        this.sadness = sadness;
        this.happiness = happiness;
        this.fear = fear;
        this.neutrality = neutrality;
        this.anger = anger;
        this.call = call;
        this.speakerType = speakerType;
    }

    public Emotion getPrimaryEmotion() {
        double maxEmotion = Collections.max(Arrays.asList(this.sadness, this.happiness, this.fear, this.neutrality, this.anger));
        if (maxEmotion == sadness){
            return Emotion.EMOTION_SADNESS;
        } else if (maxEmotion == happiness){
            return Emotion.EMOTION_HAPPINESS;
        } else if (maxEmotion == fear){
            return Emotion.EMOTION_FEAR;
        } else if (maxEmotion == neutrality){
            return Emotion.EMOTION_NEUTRALITY;
        }
        return Emotion.EMOTION_ANGER;
    }
}
