package com.intellectus.repositories;

import com.intellectus.model.Weather;
import com.intellectus.model.configuration.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
}

