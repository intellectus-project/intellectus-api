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
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    public CallResponsePatchDto update(CallRequestPatchDto callDto, Long id) throws Exception {
        Optional<Call> optionalCall = callRepository.findById((id));
        if (optionalCall.isEmpty())
            throw new Exception("Call does not exist");
        Call call = optionalCall.get();
        call.setEndTime(callDto.getEndTime());
        //todo: handle inexistent emotion exception
        call.setEmotion(Emotion.valueOf(callDto.getEmotion()).get());
        callRepository.save(call);

        AtomicReference<Boolean> breakAssigned = new AtomicReference<>(false);
        AtomicReference<Integer> minutesDuration = new AtomicReference<>();

        Optional<Break> breakOpt = breakService.findByCall(call);
        breakOpt.ifPresent(breakObj -> {
            breakObj.setActive(true);
            breakService.save(breakObj);
            breakAssigned.set(true);
            minutesDuration.set(breakObj.getMinutesDuration());
        });

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
        return CallResponsePatchDto.builder()
                .breakAssigned(breakAssigned.get())
                .minutesDuration(minutesDuration.get())
                .build();
    }

    public Call actualOperatorCall(User operator) {
        return callRepository.findActualByOperator(operator.getId());
    }

    public Collection<CallInfoDto> fetchByDateAndSupervisor(LocalDate dateFrom, LocalDate dateTo, Long supervisorId, Optional<Long> operatorId) {
        List<Call> calls = callsBySupervisor(dateFrom, dateTo, supervisorId, operatorId);
        List<CallInfoDto> dtos = calls
                .stream()
                .map(Call::toDto)
                .collect(Collectors.toList());

        dtos.forEach(callInfoDto -> {
            callInfoDto.setWeather(weatherService.getWeatherAt(callInfoDto.getStartTime()));
        });
        return dtos;
    }

    public List<Call> callsBySupervisor(LocalDate dateFrom, LocalDate dateTo, Long supervisorId, Optional<Long> operatorId) {
        if (operatorId.isPresent())
            return callRepository.findAllByUser_Supervisor_IdAndStartTimeBetweenAndEndTimeIsNotNullAndUserIdAndUser_ActiveOrderByStartTimeDesc(supervisorId,
                dateFrom.atStartOfDay().plusHours(3),
                dateTo.atTime(LocalTime.MAX).plusHours(3),
                operatorId,
          true);
        return callRepository.findAllByUser_Supervisor_IdAndStartTimeBetweenAndEndTimeIsNotNullAndUser_ActiveIsTrueOrderByStartTimeDesc(supervisorId,
                dateFrom.atStartOfDay().plusHours(3),
                dateTo.atTime(LocalTime.MAX).plusHours(3));
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
                   .startTime(call.getStartTime().minusHours(3))
                   .endTime(call.getEndTime().minusHours(3))
                   .weather(weather)
                   .shift(call.getUser().getShift())
                   .operator(new ReducedUserInfoDto(call.getUser().getId(), call.getUser().getName()))
                   .breakDurationMinutes(breakOpt.isPresent() ? breakOpt.get().getMinutesDuration() : 0)
                   .breakTaken(breakOpt.isPresent())
                   .weatherImage(weatherService.getWeatherImage(weather.getDescription(), call.getStartTime().minusHours(3).getHour()).get())
                   .build();

    }

    public Call lastOperatorCall(User operator) {
        return callRepository.findLastByOperator(operator.getId());
    }

    public List<CallInfoDto> fetchByDateAndOperator(LocalDate date, Long id){
        LocalDateTime start = date.atStartOfDay().plusHours(3);
        LocalDateTime end = date.atTime(LocalTime.MAX).plusHours(3);
        List<Call> calls = callRepository.findAllByUser_IdAndStartTimeBetweenOrderByStartTimeDesc(id, start, end);
        return calls
                .stream()
                .filter(c -> c.getStartTime() != null && c.getEndTime() != null)
                .map(Call::toDto)
                .collect(Collectors.toList());
    }
}
