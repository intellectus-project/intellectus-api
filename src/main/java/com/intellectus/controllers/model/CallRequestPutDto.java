package com.intellectus.controllers.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CallRequestPutDto {
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    private String emotion;
    @NotNull
    private StatDto consultantStats;
    @NotNull
    private StatDto operatorStats;
}
