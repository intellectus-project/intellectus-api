package com.atixlabs.services.impl;

import com.atixlabs.model.configuration.Role;
import com.atixlabs.model.configuration.User;
import com.atixlabs.repositories.RoleRepository;
import com.atixlabs.repositories.UserRepository;
import com.atixlabs.services.filters.FilterUserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserFiltersServiceTest {

    @Autowired
    private UserService service;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository repository1;

    @AfterEach
    public void deleteAllUsers() {
        repository1.deleteAll(repository1.findAllByUsernameContainsOrNameContainsOrLastNameContains("test", "", ""));
    }

    @BeforeEach
    public void returnAllUsers() {
        repository1.deleteAll();
        Role roleAnalyst = roleRepository.findByCode(com.atixlabs.model.constants.Role.ROLE_ANALYST.role());
        Role roleViewer = roleRepository.findByCode(com.atixlabs.model.constants.Role.ROLE_VIEWER.role());
        User user1 = new User(123L, "Test1Name", "Lastname1", "test1", "", roleAnalyst);
        user1.setActive(false);
        User user2 = new User(124L, "Test2Name", "Lastname2", "test2", "", roleViewer);
        User user3 = new User(125L, "Test3Name", "Lastname3", "test3", "", roleAnalyst);
        User user4 = new User(126L, "Test4Name", "Lastname4", "test4", "", roleViewer);
        List<User> userList = Lists.newArrayList(user1, user2, user3, user4);
        repository1.saveAll(userList);
    }

    /*
        Test about find user with different filters by like name, username or lastname, active, role, country or zone
     */
    @Test
    void findAllWithFilters() {
        int size = service.findAll().size();
        Assertions.assertEquals(4, service.findAll().size());
        size = service.findAll(FilterUserDto.builder().search("Lastname1").build()).size();
        Assertions.assertEquals(1, size);
        size = service.findAll(FilterUserDto.builder().search("test").enabled(Optional.of(true)).build()).size();
        Assertions.assertEquals(3, size);
        size = service.findAll(FilterUserDto.builder().search("test").enabled(Optional.of(false)).build()).size();
        Assertions.assertEquals(1, size);
        size = service.findAll(FilterUserDto.builder().search("test").role(Optional.of("Analyst")).build()).size();
        Assertions.assertEquals(2, size);
        size = service.findAll(FilterUserDto.builder().search("test").role(Optional.of("naLYS")).build()).size();
        Assertions.assertEquals(2, size);
        size = service.findAll(FilterUserDto.builder().search("test").zone(Optional.of("urop")).build()).size();
        Assertions.assertEquals(1, size);
        size = service.findAll(FilterUserDto.builder().search("test").country(Optional.of("Canary")).build()).size();
        Assertions.assertEquals(1, size);
    }

}
