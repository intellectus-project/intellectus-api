package com.intellectus.repositories;

import com.intellectus.model.Stat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

    //@Query("from Stat inner join Call on Stat.call = Call where Call.startTime  >=  :dateFrom and Call.endTime <= :dateTo")
    //List<Stat> findStatsFromCallsBetweenDate(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);
}

