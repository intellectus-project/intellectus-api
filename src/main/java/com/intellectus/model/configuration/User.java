package com.intellectus.model.configuration;

import com.intellectus.model.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AuditableEntity {

    private static final PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Email(message = "Email invalid")
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @JoinColumn(name = "ID_ROLE")
    @ManyToOne
    private Role role;

    @NotNull(message = "Active cannot be null")
    @Column(name = "ACTIVE")
    private boolean active;

    public User() {
        this.active = true;
    }

    public User(Long id) { this.id = id; }

    public User(Long id, String name, String lastName, String username, String password, Role role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = true;
    }

    @PrePersist
    public void prePersist(){
        password = pwEncoder.encode(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return active == user.active &&
                Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(username, user.username) &&
                role.equals(user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, email, phone, username, password, role, active);
    }
}
