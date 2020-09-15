package com.intellectus.repositories;

import com.intellectus.model.WeatherImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherImageRepository extends CrudRepository<WeatherImage, Long> {
    Optional<WeatherImage> findByDescription(String description);
    Optional<WeatherImage> findByDescriptionAndMinHourAndMaxHour(String description, int minHour, int maxHour);
}
