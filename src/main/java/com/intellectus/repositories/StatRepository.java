package com.intellectus.repositories;

import com.intellectus.controllers.model.BarsChartDto;
import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Emotion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    Optional<Stat> findLastByOperator(@Param("id") Long id);

    @Query("select new com.intellectus.controllers.model.BarsChartDto( avg(s.sadness), " +
            " avg(s.happiness)," +
            " avg(s.fear)," +
            " avg(s.neutrality)," +
            " avg(s.anger)," +
            " s.call.occurrenceDay ) " +
            "from Stat s " +
            "where s.call.user = :user " +
            "and s.call.startTime >= :dateFrom " +
            "and s.call.startTime <= :dateTo " +
            "group by  s.call.occurrenceDay")
    List<BarsChartDto> findStatsForUserGroupedByDateBetween(@Param("user") User user, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

    @Query("select new com.intellectus.controllers.model.BarsChartDto( avg(s.sadness), " +
            " avg(s.happiness)," +
            " avg(s.fear)," +
            " avg(s.neutrality)," +
            " avg(s.anger)," +
            " s.call.occurrenceDay ) " +
            "from Stat s " +
            "where s.call.startTime >= :dateFrom " +
            "and s.call.startTime <= :dateTo " +
            "group by  s.call.occurrenceDay")
    List<BarsChartDto> findStatsGroupedByDateBetween(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

    @Query(value = "select * from stats s " +
            "join calls c ON c.id = id_call " +
            "where c.id_user = :id AND speaker_type = 'SPEAKER_TYPE_OPERATOR' " +
            "AND c.occurrence_day = :date " +
            "order by c.created asc ",
            nativeQuery = true)
    List<Stat> findAllByOperatorAndDate(@Param("id") Long id, @Param("date") LocalDate date);
}

