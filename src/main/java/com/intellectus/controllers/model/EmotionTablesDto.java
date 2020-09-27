package com.intellectus.controllers.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.intellectus.model.Call;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class EmotionTablesDto {
    private List<Double> happiness;
    private List<Double> fear;
    private List<Double> anger;
    private List<Double> neutrality;
    private List<Double> sadness;

    public EmotionTablesDto() {
        happiness = new ArrayList<>();
        fear = new ArrayList<>();
        anger = new ArrayList<>();
        neutrality = new ArrayList<>();
        sadness = new ArrayList<>();
    }

    public void addHappiness(Double value) {
        happiness.add(value);
    }

    public void addFear(Double value) {
        fear.add(value);
    }

    public void addAnger(Double value) {
        anger.add(value);
    }

    public void addNeutrality(Double value) {
        neutrality.add(value);
    }

    public void addSadness(Double value) {
        sadness.add(value);
    }
}