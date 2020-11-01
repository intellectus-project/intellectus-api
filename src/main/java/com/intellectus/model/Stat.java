package com.intellectus.model;

import com.intellectus.controllers.model.EmotionDto;
import com.intellectus.controllers.model.StatDto;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.model.constants.Emotion;

import com.intellectus.utils.NumberUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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

    public EmotionDto getSortedMap(int index) {
        Map<Emotion, Double> emotions = Map.ofEntries(
            Map.entry(Emotion.EMOTION_SADNESS, sadness),
            Map.entry(Emotion.EMOTION_HAPPINESS, happiness),
            Map.entry(Emotion.EMOTION_FEAR, fear),
            Map.entry(Emotion.EMOTION_NEUTRALITY, neutrality),
            Map.entry(Emotion.EMOTION_ANGER, anger)
        );

        Map<Emotion, Double> java8Approach = emotions.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return new EmotionDto(new ArrayList<>(java8Approach.keySet()).get(index),
                new ArrayList<>(java8Approach.values()).get(index));
    }

    public EmotionDto getPrimaryEmotion() {
        return getSortedMap(4);
    }

    public EmotionDto getSecondaryEmotion() {
        return getSortedMap(3);
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

    public StatDto toDto() {
        return StatDto.builder()
                      .neutrality(this.neutrality)
                      .happiness(this.happiness)
                      .fear(this.fear)
                      .anger(this.anger)
                      .sadness(this.sadness)
                      .build();

    }

}
