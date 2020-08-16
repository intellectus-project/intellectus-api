package com.intellectus.services;

import com.intellectus.controllers.model.*;

import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Emotion;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.repositories.CallRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.intellectus.services.impl.UserServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class CallService {

    @Autowired
    public CallService(CallRepository callRepository, StatService statService, UserServiceImpl userService){
        this.callRepository = callRepository;
        this.statService = statService;
    }

    private CallRepository callRepository;
    private StatService statService;


    public Long create(User user, CallRequestPostDto callDto) {
        Call call = new Call(user, callDto.getStartTime(), callDto.getStartTime().toLocalDate());
        callRepository.save(call);
        return call.getId();
    }

    public void update(CallRequestPatchDto callDto, Long id) throws Exception{
        Optional<Call> optionalCall = callRepository.findById((id));
        if(optionalCall.isEmpty())
            throw new Exception("Call does not exist");
        Call call = optionalCall.get();
        call.setEndTime(callDto.getEndTime());
        //todo: handle inexistent emotion exception
        call.setEmotion(Emotion.valueOf(callDto.getEmotion()).get());

        callRepository.save(call);

        StatDto consultantDto = callDto.getConsultantStats();
        Stat consultantStats = new Stat(consultantDto.getSadness(),
                consultantDto.getHappiness(),
                consultantDto.getFear(),
                consultantDto.getNeutrality(),
                consultantDto.getAnger(),
                call,
                SpeakerType.SPEAKER_TYPE_CONSULTANT);
        statService.create(consultantStats);

        StatDto operatorDto = callDto.getOperatorStats();
        Stat operatorStats = new Stat(operatorDto.getSadness(),
                operatorDto.getHappiness(),
                operatorDto.getFear(),
                operatorDto.getNeutrality(),
                operatorDto.getAnger(),
                call,
                SpeakerType.SPEAKER_TYPE_OPERATOR);
        statService.create(operatorStats);
    }

    public Call actualOperatorCall(User operator) {
        return callRepository.findActualByOperator(operator.getId());
    }

    public Collection<CallInfoDto> fetchByDate(LocalDate dateFrom, LocalDate dateTo, Long supervisorId) {
        return callRepository.fetchByDate(dateFrom.atStartOfDay(), dateTo.atTime(LocalTime.MAX), supervisorId);
    }
}
