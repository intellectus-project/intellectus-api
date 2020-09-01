package com.intellectus.repositories;

import com.intellectus.controllers.model.CallInfoDto;
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

    @Query(value = "select * from calls c " +
            "where c.id_user = :id AND end_time iS NULL " +
            "order by c.created desc " +
            "limit 1",
            nativeQuery = true)
    Call findActualByOperator(@Param("id") Long id);

    List<Call> findAllByUser_Supervisor_IdAndStartTimeBetween(Long supervisorId, LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Call> findAllByStartTimeBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query(value = "select * from calls c " +
            "where c.id_user = :id AND end_time iS NOT NULL " +
            "order by c.created desc " +
            "limit 1",
            nativeQuery = true)
    Call findLastByOperator(@Param("id") Long id);
}
