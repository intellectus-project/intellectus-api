package com.atixlabs.services.impl;

import com.atixlabs.controllers.model.MenuDto;
import com.atixlabs.controllers.model.UserEditRequest;
import com.atixlabs.exceptions.ExistUserException;
import com.atixlabs.exceptions.InactiveUserException;
import com.atixlabs.controllers.model.AuthenticatedUserDto;
import com.atixlabs.model.configuration.User;
import com.atixlabs.services.filters.FilterUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    Collection<User> findAll();

    User disable(Long id);

    User enable(Long id);

    User createOrEdit(UserEditRequest userResponse) throws ExistUserException;

    Optional<User> findByUsername(String username);

    Optional<AuthenticatedUserDto> findUserAuthenticated(String username, String password) throws InactiveUserException;

    Collection<String> findPermissions(Long id);

    Set<MenuDto> findNavbar(Long id);

    User findById(Long id);

    Optional<User> updateUser(Map<String, Object> updates, Long id) throws Exception;

    Collection<User> findAll(FilterUserDto filter);

    Page<User> findUsersFilteredAndPaginated(FilterUserDto filter, Pageable page);

}
