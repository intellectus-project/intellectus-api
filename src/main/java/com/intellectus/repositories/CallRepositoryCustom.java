package com.intellectus.repositories;

import com.intellectus.controllers.model.CallInfoDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CallRepositoryCustom {
    List<CallInfoDto> fetchByDate(LocalDateTime dateFrom, LocalDateTime dateTo, Long id);
}
