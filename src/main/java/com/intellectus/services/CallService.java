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
