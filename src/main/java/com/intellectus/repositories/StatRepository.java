package com.intellectus.repositories;

import com.intellectus.controllers.model.BarsChartDto;
import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Emotion;
import com.intellectus.model.constants.SpeakerType;
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

    @Query(value = "select avg(s.sadness) sadness, " +
            " avg(s.happiness) happiness," +
            " avg(s.fear) fear," +
            " avg(s.neutrality) neutrality," +
            " to_char(DATE_TRUNC(:dateGroup, c.occurrence_day), 'YYYY/MM/DD') occurrenceDayTrunc " +
            "from stats s " +
            "join calls c ON c.id = s.id_call " +
            "join users u ON u.id = c.id_user " +
            "where c.start_time >= :dateFrom " +
            "and c.start_time <= :dateTo " +
            "and s.speaker_type = 'SPEAKER_TYPE_CONSULTANT' " +
            "and u.id_supervisor = :supervisorId " +
            "group by occurrenceDayTrunc " +
            "order by occurrenceDayTrunc ", nativeQuery = true)
    List<BarsChart> findStatsGroupedByDateBetween(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("dateGroup") String dateGroup,
                                                  @Param("supervisorId") Long supervisorId);

    @Query(value = "select * from stats s " +
            "join calls c ON c.id = id_call " +
            "where c.id_user = :id AND speaker_type = 'SPEAKER_TYPE_OPERATOR'" +
            "order by c.created desc " +
            "limit 1",
          nativeQuery = true)
    Optional<Stat> findLastByOperator(@Param("id") Long id);


    @Query(value = "select avg(s.sadness) sadness, " +
            " avg(s.happiness) happiness," +
            " avg(s.fear) fear," +
            " avg(s.neutrality) neutrality," +
            " to_char(DATE_TRUNC(:dateGroup, c.occurrence_day), 'YYYY/MM/DD') occurrenceDayTrunc " +
            "from stats s " +
            "join calls c ON c.id = s.id_call " +
            "where c.start_time >= :dateFrom " +
            "and c.start_time <= :dateTo " +
            "and s.speaker_type = 'SPEAKER_TYPE_CONSULTANT' " +
            "and c.id_user = :userId " +
            "group by occurrenceDayTrunc " +
            "order by occurrenceDayTrunc ", nativeQuery = true)
    List<BarsChart> findStatsForUserGroupedByDateBetween(@Param("userId") Long userId, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo,
                                                            @Param("dateGroup") String dateGroup);

    @Query(value = "select * from stats s " +
            "join calls c ON c.id = id_call " +
            "where c.id_user = :id AND speaker_type = 'SPEAKER_TYPE_OPERATOR' " +
            "AND c.occurrence_day = :date " +
            "order by c.created asc ",
            nativeQuery = true)
    List<Stat> findAllByOperatorAndDate(@Param("id") Long id, @Param("date") LocalDate date);

    Stat findByCallAndSpeakerType(Call call, SpeakerType speakerType);

    @Query(value = "select avg(s.sadness) sadness, " +
            " avg(s.happiness) happiness," +
            " avg(s.fear) fear," +
            " avg(s.neutrality) neutrality," +
            " c.id_user idUser " +
            "from stats s " +
            "join calls c ON c.id = s.id_call " +
            "join users u ON u.id = c.id_user " +
            "where c.start_time >= :dateFrom " +
            "and c.start_time <= :dateTo " +
            "and s.speaker_type = 'SPEAKER_TYPE_OPERATOR' " +
            "and u.id_supervisor = :supervisorId " +
            "group by c.id_user", nativeQuery = true)
    List<BarsChart> findStatsForGroupedByOperators(@Param("supervisorId") Long supervisorId, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

    interface BarsChart {
        double getSadness();
        double getHappiness();
        double getFear();
        double getNeutrality();
        String getOccurrenceDayTrunc();
        Long getIdUser();
    }
}

