package com.intellectus.services.impl;

import com.intellectus.model.configuration.Role;
import com.intellectus.model.configuration.User;
import com.intellectus.repositories.UserRepository;

import com.intellectus.exceptions.InexistentUserException;

import org.junit.Assert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Test
    void findAll() {
        Mockito.when(repository.findAll()).thenReturn(Stream.of(
                new User(376L, "Eric","AA", "username","$2y$12$IAQ4rJsx0Zu5cCphS3jrju7WE4QcUrkF/ZkwZukjMTQiTuMQu0tby", Role.builder().code(com.intellectus.model.constants.Role.ROLE_ADMIN.role()).id(1).build()),
                new User(375L, "Juan", "BB", "username2","$2y$12$IAQ4rJsx0Zu5cCphS3jrju7WE4QcUrkF/ZkwZukjMTQiTuMQu0tby", Role.builder().code(com.intellectus.model.constants.Role.ROLE_ADMIN.role()).id(1).build())
        ).collect(Collectors.toList()));
        int size = service.findAll().size();
        Assertions.assertEquals(2, service.findAll().size());
    }

    //TODO: write all other methods to test.

    @Test
    void updateValidUser() {
        long userId = 10;
        User user = new User(10L, "oldName", "oldSurname", "oldUsername", "", Role.builder().code(com.intellectus.model.constants.Role.ROLE_ADMIN.role()).id(1).build());
        Mockito.when(repository.findById(userId)).thenReturn(Optional.of(user));
        User userExpected = new User(10L, "newName", "newSurname", "newUsername@atixlabs.com", "", Role.builder().code(com.intellectus.model.constants.Role.ROLE_SUPERVISOR.role()).build());
        userExpected.setPhone("1140515445");
        userExpected.setEmail("newUsername@atixlabs.com");
        Optional<User> retrievedUser = Optional.empty();

        Map<String, Object> updates = new HashMap<>();
        updates.put("confirmNewPassword", "nPass12345");
        updates.put("selectedKey", "2");
        updates.put("lastName", "newSurname");
        updates.put("role", "ROLE_ANALYST");
        updates.put("phone", "1140515445");
        updates.put("name", "newName");
        updates.put("newPassword", "nPass12345");
        updates.put("type", "ZONES");
        updates.put("email", "newUsername@atixlabs.com");
        updates.put( "username", "newUsername@atixlabs.com");
        try {
            retrievedUser =  service.updateUser(updates,userId);
        }catch (Exception e){
            Assert.fail("exception during update");
        }
        if (!retrievedUser.isPresent()) Assert.fail();
        Assertions.assertTrue(userExpected.equals(retrievedUser.get()));
    }

    @Test
    void updateInexistentUserAndExpectInexistentUserException() {
        long userId = 10;
        Mockito.when(repository.findById(userId)).thenReturn(Optional.empty());
        Map<String, Object> updates = new HashMap<>();
        Optional<User> retrievedUser;
        InexistentUserException ex =
                Assertions.assertThrows(InexistentUserException.class,
                        () -> service.updateUser(updates,userId),
                        "Expected to throw inexistent user");
    }

    @Test
    void updateExistentUserWithExistentMailExpectToDoNothing() {
        String username = "estoppel@atixlabs.com";
        long idUser = 10L;
        User user = new User();
        user.setUsername(username);
        Mockito.when(repository.findById(idUser)).thenReturn(Optional.of(user));
        Mockito.when(repository.findByUsername(username)).thenReturn(user);
        Map<String, Object> updates = new HashMap<>();
        updates.put("username", username);
        Optional<User> retrievedUser = Optional.empty();
        try{
            retrievedUser = service.updateUser(updates,idUser);
        }catch (Exception e){
            Assert.fail("exception during update");
        }

        Assertions.assertEquals(retrievedUser,Optional.empty());
    }
}