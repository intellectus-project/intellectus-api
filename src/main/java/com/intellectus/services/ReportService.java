package com.intellectus.services;

import com.intellectus.controllers.model.BarsChartDto;
import com.intellectus.controllers.model.RingsChartDto;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Emotion;
import com.intellectus.model.constants.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intellectus.services.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    public ReportService(StatService statService, UserServiceImpl userService){
        this.statService = statService;
        this.userService = userService;
    }

    private StatService statService;
    private UserServiceImpl userService;

    private static final int happinessEnum = Emotion.EMOTION_HAPPINESS.getId();
    private static final int fearEnum = Emotion.EMOTION_FEAR.getId();
    private static final int angerEnum = Emotion.EMOTION_ANGER.getId();
    private static final int neutralityEnum = Emotion.EMOTION_NEUTRALITY.getId();
    private static final int sadnessEnum = Emotion.EMOTION_SADNESS.getId();

    public RingsChartDto getRingsChart(LocalDate dateFrom, LocalDate dateTo){
        List<Stat> stats = statService.getStatsBetweenDates(dateFrom, dateTo);
        Map<Integer, Double> emotionMap = new HashMap<>();

        stats.forEach(stat -> {
            emotionMap.put(happinessEnum, emotionMap.get(happinessEnum)  == null ? 0.0 + stat.getHappiness() : emotionMap.get(happinessEnum) + stat.getHappiness());
            emotionMap.put(fearEnum, emotionMap.get(fearEnum) == null ? 0.0 + stat.getFear() : emotionMap.get(fearEnum) + stat.getFear() );
            emotionMap.put(angerEnum, emotionMap.get(angerEnum) == null ? 0.0 + stat.getAnger() : emotionMap.get(angerEnum) + stat.getAnger());
            emotionMap.put(neutralityEnum, emotionMap.get(neutralityEnum) == null ? 0.0 + stat.getNeutrality() : emotionMap.get(neutralityEnum) + stat.getNeutrality());
            emotionMap.put(sadnessEnum, emotionMap.get(sadnessEnum) == null ? 0.0 + stat.getSadness() : emotionMap.get(sadnessEnum) + stat.getSadness());
        });

        int numberOfRecords = stats.size();

        double happinessAvg = numberOfRecords == 0 ? 0 : emotionMap.get(happinessEnum) / numberOfRecords;
        double fearAvg = numberOfRecords == 0 ? 0 : emotionMap.get(fearEnum)  / numberOfRecords;
        double angryAvg = numberOfRecords == 0 ? 0 : emotionMap.get(angerEnum) / numberOfRecords;
        double neutralityAvg = numberOfRecords == 0 ? 0 : emotionMap.get(neutralityEnum) / numberOfRecords;
        double sadnessAvg = numberOfRecords == 0 ? 0 : emotionMap.get(sadnessEnum) / numberOfRecords;

        return RingsChartDto.builder()
                .anger(angryAvg)
                .fear(fearAvg)
                .happiness(happinessAvg)
                .neutrality(neutralityAvg)
                .sadness(sadnessAvg)
                .numberOfStats(numberOfRecords)
                .build();
    }

    public List<BarsChartDto> getBarsChart(LocalDate dateFrom, LocalDate dateTo, Optional<Long> operatorId){
        Optional<User> user = Optional.empty();
        if (operatorId.isPresent()){
            user = Optional.of(userService.findById(operatorId.get()));
            if(!user.get().getRole().getCode().equals(Role.ROLE_OPERATOR.role())) throw new RuntimeException("The specified user id must correspond to a supervisor");
        }
        return statService.getStatsBetween(user, dateFrom, dateTo);
    }
}
