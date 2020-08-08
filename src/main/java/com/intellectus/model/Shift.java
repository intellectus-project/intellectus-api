package com.intellectus.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "shifts")
public class Shift extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    private String name;
    private Integer startHour;
    private Integer endHour;

    public Shift() {}

    public Shift(String name, Integer startHour, Integer endHour) {
        this.name = name;
        this.startHour = startHour;
        this.endHour = endHour;
    }
}
