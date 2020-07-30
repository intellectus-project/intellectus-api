package com.intellectus.model;

import com.intellectus.model.configuration.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    protected Call(){
        //required by hibernate
    }

    public Call(String clientName, User user, LocalDateTime startTime, LocalDateTime endTime){
        this.clientName = clientName;
        this.user = user;
        this.startTime = startTime;
        this.endTime = startTime;
    }
}
