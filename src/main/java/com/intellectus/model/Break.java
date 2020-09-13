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

    @Column(columnDefinition = "int default 10")
    private int minutesDuration;

    public Break() {}

    public Break(Call call, int minutesDuration) {
        this.call = call;
        this.user = call.getUser();
        this.minutesDuration = minutesDuration;
    }

}