package com.intellectus.model;

import com.intellectus.model.configuration.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "breaks")
public class Break extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @JoinColumn(name = "ID_USER")
    @ManyToOne
    private User user;

    @JoinColumn(name = "ID_CALL")
    @ManyToOne
    private Call call;

    private LocalDateTime created;
    private LocalDateTime updated;

    @Column(columnDefinition = "int default 10")
    private int minutesDuration;

    @Column(columnDefinition = "bool default false")
    private boolean givenBySupervisor;

    @Column(columnDefinition = "bool default true")
    private boolean active;

    public Break() {}

    public Break(Call call, int minutesDuration) {
        this.call = call;
        this.user = call.getUser();
        this.minutesDuration = minutesDuration;
    }

    public Break(Call call, int minutesDuration, boolean givenBySupervisor, boolean active) {
        this.call = call;
        this.user = call.getUser();
        this.minutesDuration = minutesDuration;
        this.givenBySupervisor = givenBySupervisor;
        this.active = active;
    }

}