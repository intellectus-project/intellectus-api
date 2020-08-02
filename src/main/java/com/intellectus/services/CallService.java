package com.intellectus.services;

import com.intellectus.controllers.model.CallRequestPatchDto;
import com.intellectus.controllers.model.CallRequestPostDto;
import com.intellectus.controllers.model.RingsChartDto;
import com.intellectus.controllers.model.StatDto;
import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.repositories.CallRepository;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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
            emotionMap.put("happiness", emotionMap.get("happiness") + stat.getHappiness());
            emotionMap.put("fear", emotionMap.get("fear") + stat.getFear());
            emotionMap.put("angry", emotionMap.get("angry") + stat.getAnger());
            emotionMap.put("neutral", emotionMap.get("neutral") + stat.getNeutrality());
            emotionMap.put("sadness", emotionMap.get("sadness") + stat.getSadness());
        });

        int numberOfRecords = stats.size();

        double happinessAvg = emotionMap.get("happiness") / numberOfRecords;
        double fearnessAvg = emotionMap.get("fear")  / numberOfRecords;
        double angrynessAvg = emotionMap.get("angry") / numberOfRecords;
        double neutralityAvg = emotionMap.get("neutral") / numberOfRecords;
        double sadnessAvg = emotionMap.get("sadness") / numberOfRecords;

        return RingsChartDto.builder()
                .anger(angrynessAvg)
                .fear(fearnessAvg)
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
