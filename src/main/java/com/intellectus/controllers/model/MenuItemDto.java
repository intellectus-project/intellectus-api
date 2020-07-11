package com.intellectus.controllers.model;

import com.intellectus.model.configuration.Menu;
import lombok.Getter;

@Getter
public class MenuItemDto extends MenuDto{

    private String type;

    public MenuItemDto(Menu menu) {
        super(menu);
        this.type = menu.getType();
    }
}
