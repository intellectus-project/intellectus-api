package com.intellectus.controllers.model;

import com.intellectus.model.Call;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BreakDto  {
    private Call call;
    private LocalDateTime created;
    private Integer duration;
    private Boolean givenBySupervisor;

    public BreakDto(Call call, LocalDateTime created, Integer duration, Boolean givenBySupervisor)
    {
        this.call = call;
        this.created = created.minusHours(3);
        this.duration = duration;
        this.givenBySupervisor = givenBySupervisor;
    }
}
