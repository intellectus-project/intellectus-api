package com.intellectus.services;

import com.intellectus.controllers.model.CallRequestPatchDto;
import com.intellectus.controllers.model.CallRequestPostDto;
import com.intellectus.controllers.model.RingsChartDto;

import com.intellectus.controllers.model.StatDto;
import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Emotion;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.repositories.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CallService {

    @Autowired
    public CallService(CallRepository callRepository, StatService statService){
        this.callRepository = callRepository;
        this.statService = statService;
    }

    private CallRepository callRepository;
    private StatService statService;
    private static final String happinessEnum = Emotion.EMOTION_HAPPINESS.getEmotion();
    private static final String fearEnum = Emotion.EMOTION_FEAR.getEmotion();
    private static final String angerEnum = Emotion.EMOTION_ANGER.getEmotion();
    private static final String neutralityEnum = Emotion.EMOTION_NEUTRALITY.getEmotion();
    private static final String sadnessEnum = Emotion.EMOTION_SADNESS.getEmotion();

    public RingsChartDto getRingsChart(LocalDate dateFrom, LocalDate dateTo){
        List<Stat> stats = statService.getStatsBetweenDates(dateFrom, dateTo);
        Map<String, Double> emotionMap = new HashMap<>();

        stats.forEach(stat -> {
            emotionMap.put(happinessEnum, emotionMap.get(happinessEnum)  == null ? 0.0 + stat.getHappiness() : emotionMap.get(happinessEnum) + stat.getHappiness());
            emotionMap.put(fearEnum, emotionMap.get(fearEnum) == null ? 0.0 + stat.getFear() : emotionMap.get(fearEnum) + stat.getFear() );
            emotionMap.put(angerEnum, emotionMap.get(angerEnum) == null ? 0.0 + stat.getAnger() : emotionMap.get(angerEnum) + stat.getAnger());
            emotionMap.put(neutralityEnum, emotionMap.get(neutralityEnum) == null ? 0.0 + stat.getNeutrality() : emotionMap.get(neutralityEnum) + stat.getNeutrality());
            emotionMap.put(sadnessEnum, emotionMap.get(sadnessEnum) == null ? 0.0 + stat.getSadness() : emotionMap.get(sadnessEnum) + stat.getSadness());
        });

        int numberOfRecords = stats.size();

        double happinessAvg = emotionMap.get(happinessEnum) / numberOfRecords;
        double fearAvg = emotionMap.get(fearEnum)  / numberOfRecords;
        double angryAvg = emotionMap.get(angerEnum) / numberOfRecords;
        double neutralityAvg = emotionMap.get(neutralityEnum) / numberOfRecords;
        double sadnessAvg = emotionMap.get(sadnessEnum) / numberOfRecords;

        return RingsChartDto.builder()
                .anger(angryAvg)
                .fear(fearAvg)
                .happiness(happinessAvg)
                .neutrality(neutralityAvg)
                .sadness(sadnessAvg)
                .numberOfStats(numberOfRecords)
                .build();
    }

    public Long create(User user, CallRequestPostDto callDto) {
        Call call = new Call(user, callDto.getStartTime());
        callRepository.save(call);
        return call.getId();
    }

    public void update(CallRequestPatchDto callDto, Long id) throws Exception{
        Optional<Call> optionalCall = callRepository.findById((id));
        if(optionalCall.isEmpty())
            throw new Exception("Call does not exist");
        Call call = optionalCall.get();
        call.setEndTime(callDto.getEndTime());
        call.setEmotion(callDto.getEmotion());

        callRepository.save(call);

        StatDto consultantDto = callDto.getConsultantStats();
        Stat consultantStats = new Stat(consultantDto.getSadness(),
                consultantDto.getHappiness(),
                consultantDto.getFear(),
                consultantDto.getNeutrality(),
                consultantDto.getAnger(),
                call,
                SpeakerType.SPEAKER_TYPE_CONSULTANT.getSpeakerType());
        statService.create(consultantStats);

        StatDto operatorDto = callDto.getOperatorStats();
        Stat operatorStats = new Stat(operatorDto.getSadness(),
                operatorDto.getHappiness(),
                operatorDto.getFear(),
                operatorDto.getNeutrality(),
                operatorDto.getAnger(),
                call,
                SpeakerType.SPEAKER_TYPE_OPERATOR.getSpeakerType());
        statService.create(operatorStats);
    }
}
