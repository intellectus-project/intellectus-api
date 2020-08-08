package com.intellectus.repositories;

import com.intellectus.model.Shift;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ShiftRepository extends CrudRepository<Shift, Long> {

    Shift findShiftByName(@Param("name") String name);
}
