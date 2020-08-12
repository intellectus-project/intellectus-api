package com.intellectus.controllers.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BarsChartDto {
    private LocalDate date;
    private StatDto stats;

    public BarsChartDto(double sadness, double happiness, double fear, double neutrality, double anger, LocalDate date){
        stats = StatDto.builder()
                .sadness(sadness)
                .happiness(happiness)
                .fear(fear)
                .neutrality(neutrality)
                .anger(anger)
                .build();
        this.date = date;
    }
}
