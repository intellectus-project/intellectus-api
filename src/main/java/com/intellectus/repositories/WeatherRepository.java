package com.intellectus.repositories;

import com.intellectus.model.Weather;
import com.intellectus.model.configuration.Menu;
import org.hibernate.persister.entity.Loadable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
    List<Weather> findByTimeAfterAndTimeBeforeOrderByTimeDesc(LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query(value = "select * from weathers " +
            "WHERE date_trunc('hour',:paramTime\\:\\:timestamp) = date_trunc('hour', time)",
            nativeQuery = true)
    List<Weather> findByHour(@Param("paramTime") LocalDateTime paramTime);
}

