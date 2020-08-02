package com.intellectus.repositories;

import com.intellectus.controllers.model.OperatorDto;
import com.intellectus.model.NewsEvent;
import com.intellectus.model.configuration.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User>, PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

    Collection<User> findAllByUsernameContainsOrNameContainsOrLastNameContains(String filterUsername, String filterName, String filterLastName);

    @Override
    Page<User> findAll(Pageable pageable);

    Collection<User> findOperatorsBySupervisorId(@Param("id") Long id);

}
