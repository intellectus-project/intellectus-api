package com.intellectus.model.constants;

public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_ANALYST("ROLE_ANALYST"),
    ROLE_VIEWER("ROLE_VIEWER");

    private String role;

    private Role(String role) {
        this.role = role;
    }

    public String role() {
        return this.role;
    }
}
