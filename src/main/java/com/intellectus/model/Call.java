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
    @OneToOne
    private User user;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
