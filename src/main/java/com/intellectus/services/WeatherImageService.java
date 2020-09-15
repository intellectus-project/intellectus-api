package com.intellectus.services;

import com.intellectus.model.WeatherImage;
import com.intellectus.repositories.StatRepository;
import com.intellectus.repositories.WeatherImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeatherImageService {

    @Autowired
    public WeatherImageService(WeatherImageRepository repository) {
        this.repository = repository;
    }

    private WeatherImageRepository repository;

    public void create(String description, String image) {
        if (!findByDescription(description).isPresent())
            repository.save(new WeatherImage(description, image));
    }

    public void create(String description, String image, int minHour, int maxHour) {
        if(!findByDescriptionAndTime(description, minHour, maxHour).isPresent())
            repository.save(new WeatherImage(description, image, minHour, maxHour));
    }

    public Optional<WeatherImage> findByDescription(String description){
        return repository.findByDescription(description);
    }

    public Optional<WeatherImage> findByDescriptionAndTime(String description, int minHour, int maxHour){
        return repository.findByDescriptionAndMinHourAndMaxHour(description, minHour, maxHour);
    }

}