package com.intellectus.services;

import com.intellectus.controllers.model.RingsChartDto;
import com.intellectus.model.Stat;
import com.intellectus.model.constants.Emotion;
import com.intellectus.repositories.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CallService {

    @Autowired
    public CallService(CallRepository callRepository, StatService statService){
        this.callRepository = callRepository;
        this.statService = statService;
    }

    private CallRepository callRepository;
    private StatService statService;

    public RingsChartDto getRingsChart(LocalDate dateFrom, LocalDate dateTo){
        List<Stat> stats = statService.getStatsBetweenDates(dateFrom, dateTo);
        Map<String, Double> emotionMap = new HashMap<>();

        stats.forEach(stat -> {
            emotionMap.put(Emotion.EMOTION_HAPPINESS.getEmotion(), emotionMap.get(Emotion.EMOTION_HAPPINESS.getEmotion()) + stat.getHappiness());
            emotionMap.put(Emotion.EMOTION_FEAR.getEmotion(), emotionMap.get(Emotion.EMOTION_FEAR.getEmotion()) + stat.getFear());
            emotionMap.put(Emotion.EMOTION_ANGER.getEmotion(), emotionMap.get(Emotion.EMOTION_ANGER.getEmotion()) + stat.getAnger());
            emotionMap.put(Emotion.EMOTION_NEUTRALITY.getEmotion(), emotionMap.get(Emotion.EMOTION_NEUTRALITY.getEmotion()) + stat.getNeutrality());
            emotionMap.put(Emotion.EMOTION_SADNESS.getEmotion(), emotionMap.get(Emotion.EMOTION_SADNESS.getEmotion()) + stat.getSadness());
        });

        int numberOfRecords = stats.size();

        double happinessAvg = emotionMap.get(Emotion.EMOTION_HAPPINESS.getEmotion()) / numberOfRecords;
        double fearAvg = emotionMap.get(Emotion.EMOTION_FEAR.getEmotion())  / numberOfRecords;
        double angryAvg = emotionMap.get(Emotion.EMOTION_ANGER.getEmotion()) / numberOfRecords;
        double neutralityAvg = emotionMap.get(Emotion.EMOTION_NEUTRALITY.getEmotion()) / numberOfRecords;
        double sadnessAvg = emotionMap.get(Emotion.EMOTION_SADNESS.getEmotion()) / numberOfRecords;

        return RingsChartDto.builder()
                .anger(angryAvg)
                .fear(fearAvg)
                .happiness(happinessAvg)
                .neutrality(neutralityAvg)
                .sadness(sadnessAvg)
                .build();
    }
}
