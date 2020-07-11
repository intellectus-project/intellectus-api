package com.intellectus.controllers.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserEditResponse implements Serializable {

    private Long id;
    private String username;
    private String name;
    private String lastName;
    private String password;
    private String newPassword;
    private String confirmNewPassword;
    private String email;
    private String phone;
    private String role;
    private String type;
    private String selectedKey;

}
