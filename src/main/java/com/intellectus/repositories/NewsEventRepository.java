package com.intellectus.repositories;

import com.intellectus.model.NewsEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsEventRepository extends CrudRepository<NewsEvent, Integer> {
}
