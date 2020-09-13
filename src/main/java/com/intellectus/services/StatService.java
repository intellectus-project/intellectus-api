package com.intellectus.services;

import com.intellectus.controllers.model.BarsChartDto;
import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.repositories.StatRepository;
import com.intellectus.utils.NumberUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        int daysBetween = (int)ChronoUnit.DAYS.between(dateFrom, dateTo);
        String dateGroup;
        if(daysBetween < 7) {
            dateGroup = "day";
        } else if (daysBetween < 35)  {
            dateGroup = "week";
        } else {
            dateGroup = "month";
        }
        LocalDateTime df = dateFrom.atStartOfDay();
        LocalDateTime dt = dateTo.atTime(LocalTime.MAX);
        List<StatRepository.BarsChart> barsCharts =  user.isPresent() ? statRepository.findStatsForUserGroupedByDateBetween(user.get().getId(), df, dt, dateGroup)
                : statRepository.findStatsGroupedByDateBetween(df, dt, dateGroup);
        return barsCharts.stream().map(elem ->
                new BarsChartDto(elem.getSadness(),
                        elem.getHappiness(),
                        elem.getFear(),
                        elem.getNeutrality(),
                        elem.getOccurrenceDayTrunc()
                )).collect(Collectors.toList());
    }

    public List<Stat> getByOperatorAndDate(User operator, LocalDate date){
        return statRepository.findAllByOperatorAndDate(operator.getId(), date);
    }

    public Stat findByCallAndSpeakerType(Call call, SpeakerType speakerType) {
        return statRepository.findByCallAndSpeakerType(call, speakerType);
    }

}
