package com.intellectus.model.constants;

public enum WeatherType {
    RAINY("RAINY"),
    SUNNY("SUNNY"),
    CLOUDY("CLOUDY");

    private String weatherType;

    private WeatherType(String role) {
        this.weatherType = weatherType;
    }

    public String weatherType() {
        return this.weatherType;
    }
}
