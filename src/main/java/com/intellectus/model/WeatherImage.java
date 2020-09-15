package com.intellectus.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "weather_images")
public class WeatherImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    private String description;
    private String image;
    private Integer minHour;
    private Integer maxHour;

    public WeatherImage() {}

    public WeatherImage(String description, String image){
        this.description = description;
        this.image = image;
    }

    public WeatherImage(String description, String image, int minHour, int maxHour){
        this.description = description;
        this.image = image;
        this.minHour = minHour;
        this.maxHour = maxHour;
    }
}
