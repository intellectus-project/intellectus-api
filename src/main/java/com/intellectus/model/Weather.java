package com.intellectus.model;

import com.intellectus.model.configuration.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "weathers")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    private String description;
    private Double temperature;
    private LocalDateTime time;

    public Weather() { }

    public Weather(String description, Double temperature, LocalDateTime time) {
        this.description = description;
        this.temperature = temperature;
        this.time = time;
    }
}
