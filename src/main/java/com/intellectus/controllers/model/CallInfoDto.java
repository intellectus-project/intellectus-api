package com.intellectus.controllers.model;

import com.intellectus.model.Shift;
import com.intellectus.model.Weather;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
public class CallInfoDto  {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Weather weather;
    private Shift shift;
    private ReducedUserInfoDto operator;
    private Long id;
}
