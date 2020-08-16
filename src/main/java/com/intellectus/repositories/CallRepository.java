package com.intellectus.repositories;

import com.intellectus.model.Call;
import com.intellectus.model.configuration.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CallRepository extends CrudRepository<Call, Long> {

    @Query(value = "select * from calls c " +
            "where c.id_user = :id AND end_time iS NULL " +
            "order by c.created desc " +
            "limit 1",
            nativeQuery = true)
    Call findActualByOperator(@Param("id") Long id);

    @Query(value = "select * from calls c " +
            "where c.id_user = :id AND end_time iS NOT NULL " +
            "order by c.created desc " +
            "limit 1",
            nativeQuery = true)
    Call findLastByOperator(@Param("id") Long id);
}
