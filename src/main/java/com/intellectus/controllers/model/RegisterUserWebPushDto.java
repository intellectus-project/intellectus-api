package com.intellectus.controllers.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterUserWebPushDto {
    private String endpoint;
    private String p256dh;
    private String auth;
}
