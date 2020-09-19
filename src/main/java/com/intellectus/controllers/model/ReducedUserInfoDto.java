package com.intellectus.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReducedUserInfoDto {
    private Long id;
    private String name;
}
