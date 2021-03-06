package com.intellectus.services;

import com.intellectus.model.Shift;
import com.intellectus.repositories.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {

    @Autowired
    public ShiftService(ShiftRepository shiftRepository){
        this.shiftRepository = shiftRepository;
    }

    private ShiftRepository shiftRepository;

    public List<Shift> findAll() {
        return (List<Shift>) shiftRepository.findAll();
    }

    public Optional<Shift> findById(Long id) { return shiftRepository.findById(id); }
}
