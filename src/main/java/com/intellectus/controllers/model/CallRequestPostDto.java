package com.intellectus.controllers.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CallRequestPostDto {
    @NotNull
    private LocalDateTime startTime;
}