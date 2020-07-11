package com.intellectus.controllers.model;

import com.intellectus.model.constants.NavBar;
import lombok.Getter;

@Getter
public class NavbarDTO {

    private String key;
    private boolean mainMenu;
    private String keyParent;
    private String description;
    private String icon;
    private String url;
    private String type;

    public NavbarDTO(NavBar navBarItem) {
        this.key = navBarItem.getKey();
        this.mainMenu = navBarItem.isMainMenu();
        this.description = navBarItem.getDescription();
        this.icon = navBarItem.getIcon();
        this.keyParent = navBarItem.getKeyParent();
        this.url = navBarItem.getUrl();
        this.type = navBarItem.getType();

    }
}
