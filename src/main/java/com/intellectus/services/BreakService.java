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

    public void create(Call call) {
        Break breakInstance = new Break(call);
        breakRepository.save(breakInstance);
    }

    public List<BreakDto> fetchByUserAndDate(LocalDate dateFrom, LocalDate dateTo, User user) {
        List<Break> breaks = breakRepository.findAllByUserBetweenDate(dateFrom.atStartOfDay(), dateTo.atTime(LocalTime.MAX), user.getId());
        List<BreakDto> dtos = new ArrayList<>();
        breaks.forEach(b -> {
            dtos.add(new BreakDto(b.getCall(), b.getCreated()));
        });
        return dtos;
    }

    public Optional<Break> findById(Long id) { return breakRepository.findById(id); }
}

