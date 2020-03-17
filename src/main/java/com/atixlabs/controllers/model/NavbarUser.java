package com.atixlabs.controllers.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Builder
@Setter
@Getter
public class NavbarUser {

    private String logo;
    private Set<MenuDto> menus;
}
