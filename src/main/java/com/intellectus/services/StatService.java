package com.intellectus.services;

import com.intellectus.controllers.model.BarsChartDto;
import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.User;
import com.intellectus.repositories.StatRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

    public void create(Stat stat) {
        statRepository.save(stat);
    }

    public Optional<Stat> lastOperatorStat(User operator) {
        return statRepository.findLastByOperator(operator.getId());
    }

    public List<BarsChartDto> getStatsBetween(Optional<User> user, LocalDate dateFrom, LocalDate dateTo){
        LocalDateTime df = dateFrom.atStartOfDay();
        LocalDateTime dt = dateTo.atTime(LocalTime.MAX);
        return user.isPresent() ? statRepository.findStatsForUserGroupedByDateBetween(user.get(), df, dt)
                : statRepository.findStatsGroupedByDateBetween(df, dt);
    }

    public List<Stat> getByOperatorAndDate(User operator, LocalDate date){
        return statRepository.findAllByOperatorAndDate(operator.getId(), date);
    }

}
