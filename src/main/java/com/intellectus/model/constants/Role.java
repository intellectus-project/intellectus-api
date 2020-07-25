package com.intellectus.model.constants;

public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUPERVISOR("ROLE_SUPERVISOR"),
    ROLE_OPERATOR("ROLE_OPERATOR");

    private String role;

    private Role(String role) {
        this.role = role;
    }

    public String role() {
        return this.role;
    }
}
