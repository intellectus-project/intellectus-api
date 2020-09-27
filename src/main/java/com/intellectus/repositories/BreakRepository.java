package com.intellectus.repositories;

import com.intellectus.controllers.model.BreakDto;
import com.intellectus.model.Break;
import com.intellectus.model.Call;
import com.intellectus.model.configuration.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BreakRepository extends JpaRepository<Break, Long> {
    @Query(value = "select * from breaks " +
            "where breaks.id_user = :idUser AND breaks.created >= :dateFrom and breaks.created <= :dateTo" +
            "and active = true",
            nativeQuery = true)
    List<Break> findAllByUserBetweenDate(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo,
                                            @Param("idUser") Long idUser);

    Optional<Break> findByCall(Call call);

    Optional<Break> findFirstByUserAndActiveOrderByIdDesc(User user, boolean active);
}
