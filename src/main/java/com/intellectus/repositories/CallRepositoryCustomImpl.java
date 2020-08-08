package com.intellectus.repositories;

import com.intellectus.controllers.model.CallInfoDto;
import com.intellectus.model.Call;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class CallRepositoryCustomImpl extends BaseRepositoryImpl implements CallRepositoryCustom  {

    public List<CallInfoDto> fetchByDate(LocalDateTime dateFrom, LocalDateTime dateTo, Long id) {
        String hqlQuery =
                "SELECT NEW com.intellectus.controllers.model.CallInfoDto(Call.startTime, Call.endTime, Call.user.shift, Call.user) " +
                "From Call " +
                "WHERE Call.user.supervisor.id = :id AND Call.startTime >= :dateFrom and Call.endTime <= :dateTo ";

        TypedQuery<CallInfoDto> typedQuery = em.createQuery(hqlQuery , CallInfoDto.class);
        typedQuery.setParameter("dateFrom", dateFrom);
        typedQuery.setParameter("dateTo", dateTo);
        typedQuery.setParameter("id", id);
        return typedQuery.getResultList();

    }
}
