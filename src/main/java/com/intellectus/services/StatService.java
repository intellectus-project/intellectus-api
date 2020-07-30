package com.intellectus.services;

import com.intellectus.model.Stat;
import com.intellectus.repositories.StatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class StatService {

    @Autowired
    public StatService(StatRepository statRepository){
        this.statRepository = statRepository;
    }

    private StatRepository statRepository;

    public List<Stat> getStatsBetweenDates(LocalDate dateFrom, LocalDate dateTo){
        return statRepository.findStatsFromCallsBetweenDate(dateFrom.atStartOfDay(), dateTo.atTime(LocalTime.MAX));

    }
}
