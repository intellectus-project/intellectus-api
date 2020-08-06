package com.intellectus.repositories;

import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.constants.Emotion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

    @Query("from Stat s inner join Call c on s.call = c where c.startTime  >=  :dateFrom and c.startTime <= :dateTo")
    List<Stat> findStatsFromCallsBetweenDate(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

    @Query(value = "select * from stats s " +
            "join calls c ON c.id = id_call " +
            "where c.id_user = :id AND speaker_type = 'SPEAKER_TYPE_OPERATOR'" +
            "order by c.created desc " +
            "limit 1",
          nativeQuery = true)
    Stat findLastByOperator(@Param("id") Long id);
}

