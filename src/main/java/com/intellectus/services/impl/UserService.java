package com.intellectus.services.impl;

import com.intellectus.controllers.model.*;
import com.intellectus.exceptions.ExistUserException;
import com.intellectus.exceptions.InactiveUserException;
import com.intellectus.model.configuration.User;
import com.intellectus.services.filters.FilterUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

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

    Collection<User> getOperatorsBySupervisor(Long supervisorId);

    Collection<OperatorDto> getOperatorsWithInfoBySupervisor(Long supervisorId);

    void assignSupervisorToOperator(User supervisor, Long operatorId);

    List<User> getSupervisors();

    EmotionStatusDto getOperatorEmotionStatus(User operator);

    EmotionTablesDto getEmotionTables(User operator, LocalDate date);

    void registerWebPush(User user, RegisterUserWebPushDto dto);

    boolean atBreak(User user);

    long remainingBreakTime(User user);
}
