package com.intellectus.repositories;

import com.intellectus.model.configuration.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByCode(String role);
}
