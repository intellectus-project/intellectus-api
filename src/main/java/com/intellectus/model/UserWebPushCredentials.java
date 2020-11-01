package com.intellectus.model;

import com.intellectus.model.configuration.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_web_push_credentials")
public class UserWebPushCredentials extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    private String endpoint;
    private String p256dh;
    private String auth;

    @JoinColumn(name = "ID_USER")
    @ManyToOne
    private User user;
}