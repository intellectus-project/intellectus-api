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
    private Call call;

    private String speakerType;

    protected Stat(){
        //required by hib
    }
    public Stat(double sadness, double happiness, double fear, double neutrality, double anger, Call call, String speakerType){
        this.sadness = sadness;
        this.happiness = happiness;
        this.fear = fear;
        this.neutrality = neutrality;
        this.anger = anger;
        this.call = call;
        this.speakerType = speakerType;
    }


}
