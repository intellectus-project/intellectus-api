package com.intellectus.model;

import com.intellectus.model.configuration.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "weathers")
public class Weather extends AuditableEntity {

    public Weather() { }

    public Weather(String description, Double temperature) {
        this.description = description;
        this.temperature = temperature;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TEMPERATURE")
    private Double temperature;

}
