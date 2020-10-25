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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BreakService {

    @Autowired
    public BreakService(BreakRepository breakRepository){
        this.breakRepository = breakRepository;
    }

    private BreakRepository breakRepository;

    public void create(Call call, int minutesDuration) {
        Break breakInstance = new Break(call, minutesDuration);
        breakRepository.save(breakInstance);
    }

    public void create(Call call, int minutesDuration, boolean givenBySupervisor, boolean active) {
        Break breakInstance = new Break(call, minutesDuration, givenBySupervisor, active);
        breakRepository.save(breakInstance);
    }

    public List<BreakDto> fetchByUserAndDate(LocalDate dateFrom, LocalDate dateTo, User user) {
        List<Break> breaks = breakRepository.findAllByUserBetweenDate(dateFrom.atStartOfDay(), dateTo.atTime(LocalTime.MAX), user.getId());
        List<BreakDto> dtos = new ArrayList<>();
        breaks.forEach(b -> {
            dtos.add(new BreakDto(b.getCall(), b.getCreated(), b.getMinutesDuration(), b.isGivenBySupervisor()));
        });
        return dtos;
    }

    public Optional<Break> findById(Long id) { return breakRepository.findById(id); }

    public Optional<Break> findByCall(Call call) { return breakRepository.findByCall(call); }

    public Optional<Break> findLastByUser(User user, boolean active) {
        return breakRepository.findFirstByUserAndActiveOrderByIdDesc(user, active);
    }

    public void save(Break breakObj) {
        breakRepository.save(breakObj);
    }

}

