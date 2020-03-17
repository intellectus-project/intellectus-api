package com.atixlabs.model.configuration;

import com.atixlabs.model.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "permissions")
public class Permission extends AuditableEntity {

    @Id
    @Column(name = "CODE", unique = true)
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    public Permission() {
    }

    public Permission(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
