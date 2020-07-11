package com.intellectus.model;

import javax.persistence.*;

@Entity
@Table(name = "user_sessions")
public class UserSession extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
}
