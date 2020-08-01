package com.intellectus.repositories;

import com.intellectus.model.Call;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallRepository extends CrudRepository<Call, Long> {
}
