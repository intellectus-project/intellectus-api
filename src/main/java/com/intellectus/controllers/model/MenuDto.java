package com.intellectus.controllers.model;

import com.intellectus.model.configuration.Menu;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Collection;

@Getter
public class MenuDto {

    private String key;
    private String description;
    private String icon;
    private Collection<MenuItemDto> items;
    private String url;
    private Integer order;

    public MenuDto(Menu menu) {
        this.key = menu.getCode();
        this.description = menu.getName();
        this.icon = menu.getIcon();
        this.url = menu.getUri();
        this.order = menu.getOrder();
        items = Sets.newHashSet();
    }
}
