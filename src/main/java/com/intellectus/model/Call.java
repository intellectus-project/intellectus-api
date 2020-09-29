package com.intellectus.model;

import com.intellectus.controllers.model.CallInfoDto;
import com.intellectus.controllers.model.ReducedUserInfoDto;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Emotion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "calls")
public class Call extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    private String clientName;

    @JoinColumn(name = "ID_USER")
    @ManyToOne
    private User user;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate occurrenceDay;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    public Call() {}

    public Call(User user, LocalDateTime startTime, LocalDate occurrenceDay) {
        this.user = user;
        this.startTime = startTime;
        this.occurrenceDay = occurrenceDay;
    }

    public Call(User user, LocalDateTime startTime, LocalDateTime endTime, Emotion emotion) {
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.emotion = emotion;
    }
    public CallInfoDto toDto(){
        return CallInfoDto.builder()
                .shift(this.user.getShift())
                .startTime(this.startTime)
                .endTime(this.endTime)
                .operator(new ReducedUserInfoDto(this.user.getId(), this.user.getName()))
                .id(this.getId())
                .build();
    }
}
