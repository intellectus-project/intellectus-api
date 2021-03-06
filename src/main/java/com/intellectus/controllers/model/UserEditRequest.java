package com.intellectus.controllers.model;

import com.intellectus.model.Shift;
import com.intellectus.model.annotations.ValidPassword;
import com.intellectus.model.configuration.User;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class UserEditRequest implements Serializable {

    private Long id;
    private String username;
    private String name;
    private String lastName;
    private String password;
    private String newPassword;
    @ValidPassword
    private String confirmNewPassword;
    private String email;
    private String phone;
    private String role;
    private String type;
    private String selectedKey;
    private User supervisor;
    private Long shiftId;
    private Long supervisorId;
}
