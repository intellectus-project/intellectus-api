package com.intellectus.controllers.model;

import com.intellectus.model.Shift;
import com.intellectus.model.Weather;
import com.intellectus.model.configuration.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
public class CallInfoDto  {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Weather weather;
    private Shift shift;
    private User operator;

    public CallInfoDto(LocalDateTime startTime, LocalDateTime endTime, Shift shift, User operator) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.shift = shift;
        this.operator = operator;
    }
}
