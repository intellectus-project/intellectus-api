package com.intellectus.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Builder
@Setter
@Getter
public class LoginRequest implements Serializable {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
