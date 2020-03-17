package com.atixlabs.repositories;

import com.atixlabs.model.configuration.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, String> {
}
