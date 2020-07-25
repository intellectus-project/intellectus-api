package com.intellectus.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    private Call calls;

    private String speakerType;


}
