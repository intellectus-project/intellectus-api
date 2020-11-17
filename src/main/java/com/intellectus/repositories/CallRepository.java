package com.intellectus.repositories;

import com.intellectus.controllers.model.CallInfoDto;
import com.intellectus.controllers.model.CallViewDto;
import com.intellectus.model.Call;
import com.intellectus.model.configuration.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface CallRepository extends CrudRepository<Call, Long> {

    @Query(value = "select * from (" +
            "select * from calls c " +
            "where c.id_user = :id " +
            "order by c.created desc NULLS LAST " +
            "limit 1 ) sq " +
            "where end_time iS NULL",
            nativeQuery = true)
    Call findActualByOperator(@Param("id") Long id);

    List<Call> findAllByUser_Supervisor_IdAndStartTimeBetweenAndEndTimeIsNotNull(Long supervisorId, LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Call> findAllByUser_IdAndOccurrenceDay(Long operatorId, LocalDate date);

    List<Call> findAllByStartTimeBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query(value = "select * from calls c " +
            "where c.id_user = :id AND end_time iS NOT NULL " +
            "order by c.created desc " +
            "limit 1",
            nativeQuery = true)
    Call findLastByOperator(@Param("id") Long id);
}
