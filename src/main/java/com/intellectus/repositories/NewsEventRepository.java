package com.intellectus.repositories;

import com.intellectus.model.NewsEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsEventRepository extends CrudRepository<NewsEvent, Long> {

    @Query("from NewsEvent where url = :url")
    List<NewsEvent> findByUrl(@Param("url") String url);
}
