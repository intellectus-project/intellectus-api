package com.intellectus.services;

import com.intellectus.controllers.model.BreakDto;
import com.intellectus.controllers.model.NewsEventDto;
import com.intellectus.model.Break;
import com.intellectus.model.Call;
import com.intellectus.model.configuration.User;
import com.intellectus.repositories.BreakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BreakService {

    @Autowired
    public BreakService(BreakRepository breakRepository, SendWebPushNotification sendWebPushNotification){
        this.breakRepository = breakRepository;
        this.sendWebPushNotification = sendWebPushNotification;
    }

    private BreakRepository breakRepository;
    private SendWebPushNotification sendWebPushNotification;

    public void create(Call call, int minutesDuration) {
        Optional<Break> breakOpt = findByCall(call);
        breakOpt.ifPresentOrElse(breakObj -> {
            breakObj.setUpdated(LocalDateTime.now());
            save(breakObj);
        },
        () -> {
            Break breakInstance = new Break(call, minutesDuration);
            breakRepository.save(breakInstance);
            User user = call.getUser();
            sendWebPushNotification.breakTaken(user.getSupervisor(), user.getFullName());
           });
    }

    public void create(Call call, int minutesDuration, boolean givenBySupervisor, boolean active) {
        Break breakInstance = new Break(call, minutesDuration, givenBySupervisor, active);
        breakRepository.save(breakInstance);
    }

    public List<BreakDto> fetchByUserAndDate(LocalDate dateFrom, LocalDate dateTo, User user) {
        List<Break> breaks = breakRepository.findAllByUserBetweenDateOrderByIdDesc(dateFrom.atStartOfDay(), dateTo.atTime(LocalTime.MAX), user.getId());
        List<BreakDto> dtos = new ArrayList<>();
        breaks.forEach(b -> {
            dtos.add(new BreakDto(b.getCall(), b.getCreated(), b.getMinutesDuration(), b.isGivenBySupervisor(), b.getUpdated()));
        });
        return dtos.stream().sorted(Comparator.comparing(BreakDto::getCreated).reversed()).collect(Collectors.toList());
    }

    public Optional<Break> findById(Long id) { return breakRepository.findById(id); }

    public Optional<Break> findByCall(Call call) { return breakRepository.findByCall(call); }

    public Optional<Break> findLastByUser(User user, boolean active) {
        return breakRepository.findFirstByUserAndActiveOrderByIdDesc(user, active);
    }

    public void save(Break breakObj) {
        breakRepository.save(breakObj);
    }

    public boolean isActive(Break breakObj){
        return breakObj.getUpdated().plusMinutes(breakObj.getMinutesDuration()).isAfter(LocalDateTime.now());
    }

    public Long remainingBreakTime(Break breakObj){
        LocalDateTime finishBreakDateTime = breakObj.getUpdated().plusMinutes(breakObj.getMinutesDuration());
        long remaining = LocalDateTime.now().until(finishBreakDateTime, ChronoUnit.SECONDS);
        if (remaining < 0) return 0l;
        return remaining;
    }
}

