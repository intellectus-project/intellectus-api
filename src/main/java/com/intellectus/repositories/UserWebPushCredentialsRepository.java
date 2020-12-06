package com.intellectus.repositories;

import com.intellectus.model.UserWebPushCredentials;
import com.intellectus.model.configuration.Menu;
import com.intellectus.model.configuration.User;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserWebPushCredentialsRepository extends CrudRepository<UserWebPushCredentials, Integer> {

    Collection<UserWebPushCredentials> findAllByUser(User user);

}

