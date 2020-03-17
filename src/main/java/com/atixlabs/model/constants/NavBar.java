package com.atixlabs.model.constants;

import lombok.Getter;

@Getter
public enum NavBar {
    USERS("USERS", "Usuarios", true, "icon", "", ""),
    CREATE_USER("CREATE_USER", "Alta Usuario", "USERS", "icon", "/users", "POST"),
    LIST_USERS("LIST_USERS", "Usuarios", "USERS", "icon", "/users", "GET"),
    DISABLE_USER("DISABLE_USER", "Baja Usuariu", "USERS", "icon", "/users/disable/{id}", "GET"),
    MODIFY_USER("MODIFY_USER", "Modificar Usuario", "USERS", "icon", "/users/{id}", "GET"),
    VIEW_PROFILE("VIEW_PROFILE", "Perfil", "USERS", "icon", "/users/profile", "GET"),
    ;

    private String key;
    private boolean mainMenu;
    private String keyParent;
    private String description;
    private String icon;
    private String url;
    private String type;

    private NavBar(String key, String description, String keyParent, String icon, String url, String type) {
        this.key = key;
        this.description = description;
        this.keyParent = keyParent;
        this.icon = icon;
        this.url = url;
        this.type = type;
    }

    private NavBar(String key, String description, boolean mainManu, String icon, String url, String type) {
        this.key = key;
        this.mainMenu = mainManu;
        this.description = description;
        this.icon = icon;
        this.url = url;
        this.type = type;
    }
}
