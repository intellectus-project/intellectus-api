package com.intellectus.controllers.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Setter
@Getter
public class AuthenticatedUserDto {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String lastname;
    private String accessToken;
    private String tokenType = "Bearer";
    private NavbarUser navbar;
    private String role;
    private String territory;
    private Set<String> permissions;
}
