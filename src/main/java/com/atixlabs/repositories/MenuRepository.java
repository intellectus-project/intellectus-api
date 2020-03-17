package com.atixlabs.repositories;

import com.atixlabs.model.configuration.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Integer> {

    Optional<Menu> findByCode(String code);
}
