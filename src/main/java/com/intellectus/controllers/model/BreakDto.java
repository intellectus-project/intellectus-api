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

    public BreakDto(Call call, LocalDateTime created)
    {
        this.call = call;
        this.created = created;
    }
}
