package com.intellectus.controllers.model;

import com.intellectus.model.Break;
import com.intellectus.model.Shift;
import com.intellectus.model.Weather;
import com.intellectus.model.constants.Emotion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@Setter
public class CallViewDto {
    private StatDto operatorStats;
    private StatDto consultantStats;
    private Emotion emotion;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Weather weather;
    private Shift shift;
    private ReducedUserInfoDto operator;
    private boolean breakTaken;
    private int breakDurationMinutes;
}
