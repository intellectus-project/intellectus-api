package com.intellectus.services;

import com.intellectus.controllers.model.*;

import com.intellectus.model.Break;
import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.Weather;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Emotion;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.repositories.CallRepository;
import com.intellectus.services.weather.WeatherService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.intellectus.services.impl.UserServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CallService {

    @Autowired
    public CallService(CallRepository callRepository,
                       StatService statService,
                       WeatherService weatherService,
                       BreakService breakService){

        this.callRepository = callRepository;
        this.statService = statService;
        this.weatherService = weatherService;
        this.breakService = breakService;
    }

    private CallRepository callRepository;
    private StatService statService;
    private WeatherService weatherService;
    private BreakService breakService;

    public Long create(User user, CallRequestPostDto callDto) {
        Call call = new Call(user, callDto.getStartTime(), callDto.getStartTime().toLocalDate());
        callRepository.save(call);
        return call.getId();
    }

    public void update(CallRequestPatchDto callDto, Long id) throws Exception {
        Optional<Call> optionalCall = callRepository.findById((id));
        if (optionalCall.isEmpty())
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

    public Collection<CallInfoDto> fetchByDateAndSupervisor(LocalDate dateFrom, LocalDate dateTo, Long supervisorId) {
        List<Call> calls = callRepository.findAllByUser_Supervisor_IdAndStartTimeBetween(supervisorId, dateFrom.atStartOfDay(), dateTo.atTime(LocalTime.MAX));
        List<CallInfoDto> dtos = calls
                .stream()
                .map(Call::toDto)
                .collect(Collectors.toList());

        dtos.forEach(callInfoDto -> {
            callInfoDto.setWeather(weatherService.getWeatherAt(callInfoDto.getStartTime()));
        });
        return dtos;
    }

    public List<Call> fetchByDay(LocalDate date) {
        return callRepository.findAllByStartTimeBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX));
    }

    public Optional<Call> findById(Long id) {
        return callRepository.findById(id);
    }

    public CallViewDto findByIdWithInfo(Long id) {
        Call call = findById(id).get();
        Stat operatorStat = statService.findByCallAndSpeakerType(call, SpeakerType.SPEAKER_TYPE_OPERATOR);
        Stat consultantStat = statService.findByCallAndSpeakerType(call, SpeakerType.SPEAKER_TYPE_CONSULTANT);
        Weather weather = weatherService.getWeatherAt(call.getStartTime());
        Optional<Break> breakOpt = breakService.findByCall(call);
        return CallViewDto.builder()
                   .consultantStats(consultantStat.toDto())
                   .operatorStats(operatorStat.toDto())
                   .emotion(call.getEmotion())
                   .startTime(call.getStartTime())
                   .endTime(call.getEndTime())
                   .weather(weather)
                   .shift(call.getUser().getShift())
                   .operator(new ReducedUserInfoDto(call.getUser().getId(), call.getUser().getName()))
                   .breakDurationMinutes(breakOpt.isPresent() ? breakOpt.get().getMinutesDuration() : null)
                   .breakTaken(breakOpt.isPresent())
                   .build();

    }

    public Call lastOperatorCall(User operator) {
        return callRepository.findLastByOperator(operator.getId());
    }
}
