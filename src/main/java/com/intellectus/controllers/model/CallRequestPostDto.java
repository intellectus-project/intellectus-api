package com.intellectus.controllers.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CallRequestPostDto {
    @NotNull
    private LocalDateTime startTime;
}