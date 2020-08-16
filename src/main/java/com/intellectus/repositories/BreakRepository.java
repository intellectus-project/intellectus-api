package com.intellectus.repositories;

import com.intellectus.controllers.model.BreakDto;
import com.intellectus.model.Break;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BreakRepository extends JpaRepository<Break, Long> {
    @Query(value = "select * from breaks " +
            "where breaks.id_user = :idUser AND breaks.created >= :dateFrom and breaks.created <= :dateTo",
            nativeQuery = true)
    List<Break> findAllByUserBetweenDate(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo,
                                            @Param("idUser") Long idUser);
}