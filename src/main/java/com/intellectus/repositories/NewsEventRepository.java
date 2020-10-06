package com.intellectus.repositories;

import com.intellectus.controllers.model.NewsEventDto;
import com.intellectus.model.NewsEvent;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsEventRepository extends JpaRepository<NewsEvent, Long> {

    @Query("from NewsEvent where url = :url")
    List<NewsEvent> findByUrl(@Param("url") String url);

    List<NewsEventDto> findAllByCreatedBetween(LocalDateTime dateFrom, LocalDateTime dateTo);
}