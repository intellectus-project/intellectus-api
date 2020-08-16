package com.intellectus.repositories;

import com.intellectus.model.Weather;
import com.intellectus.model.configuration.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
    List<Weather> findByTimeBeforeOrderByTimeDesc(LocalDateTime date);
}

