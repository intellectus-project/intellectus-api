package com.intellectus.controllers.model;

import com.intellectus.utils.NumberUtils;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BarsChartDto {
    private String date;
    private StatDto stats;

    public BarsChartDto(double sadness, double happiness, double fear, double neutrality, String date){
        double anger = 1 - NumberUtils.roundDouble(sadness) - NumberUtils.roundDouble(happiness)
                - NumberUtils.roundDouble(fear) - NumberUtils.roundDouble(neutrality);
        stats = StatDto.builder()
                .sadness(NumberUtils.roundDouble(sadness))
                .happiness(NumberUtils.roundDouble(happiness))
                .fear(NumberUtils.roundDouble(fear))
                .neutrality(NumberUtils.roundDouble(neutrality))
                .anger(NumberUtils.roundDouble(anger))
                .build();
        this.date = date;
    }
}
