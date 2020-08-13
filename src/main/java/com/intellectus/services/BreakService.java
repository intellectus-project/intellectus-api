package com.intellectus.services;

import com.intellectus.model.Break;
import com.intellectus.model.Call;
import com.intellectus.repositories.BreakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<Break> findById(Long id) { return breakRepository.findById(id); }
}

