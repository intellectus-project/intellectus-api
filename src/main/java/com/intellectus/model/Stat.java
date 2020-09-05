package com.intellectus.model;

import com.intellectus.controllers.model.EmotionDto;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.model.constants.Emotion;

import com.intellectus.utils.NumberUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        this.sadness = NumberUtils.roundDouble(sadness);
        this.happiness = NumberUtils.roundDouble(happiness);
        this.fear = NumberUtils.roundDouble(fear);
        this.neutrality = NumberUtils.roundDouble(neutrality);
        this.anger = NumberUtils.roundDouble(anger);
        this.call = call;
        this.speakerType = speakerType;
    }

    public EmotionDto getPrimaryEmotion() {
        double maxEmotion = Collections.max(Arrays.asList(this.sadness, this.happiness, this.fear, this.neutrality, this.anger));
        return new EmotionDto(findEmotion(maxEmotion), maxEmotion);
    }

    public EmotionDto getSecondaryEmotion() {
        List<Double> emotions = Arrays.asList(this.sadness, this.happiness, this.fear, this.neutrality, this.anger);
        Collections.sort(emotions);
        double secondMaxEmotion = emotions.get(1);
        return new EmotionDto(findEmotion(secondMaxEmotion), secondMaxEmotion);
    }

    private Emotion findEmotion(double emotion){
        if (emotion == sadness){
            return Emotion.EMOTION_SADNESS;
        } else if (emotion == happiness){
            return Emotion.EMOTION_HAPPINESS;
        } else if (emotion == fear){
            return Emotion.EMOTION_FEAR;
        } else if (emotion == neutrality){
            return Emotion.EMOTION_NEUTRALITY;
        }
        return Emotion.EMOTION_ANGER;
    }
}
