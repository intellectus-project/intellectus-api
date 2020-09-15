package com.intellectus.controllers.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WeatherDto {
    private String description;
    private double currentTemperature;
    private double minTemperature;
    private double maxTemperature;
    private String image;
}
