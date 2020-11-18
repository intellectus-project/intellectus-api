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
    public LocalDateTime updated;

    public BreakDto(Call call, LocalDateTime created, Integer duration, Boolean givenBySupervisor, LocalDateTime updated)
    {
        this.call = call;
        this.created = created.minusHours(3);
        this.duration = duration;
        this.givenBySupervisor = givenBySupervisor;
        this.updated = updated.minusHours(3);
    }
}
