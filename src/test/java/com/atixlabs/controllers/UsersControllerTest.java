package com.atixlabs.controllers;

import com.atixlabs.model.configuration.Role;
import com.atixlabs.model.configuration.User;

import com.atixlabs.repositories.RoleRepository;
import com.atixlabs.repositories.UserRepository;
import com.atixlabs.services.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UsersControllerTest {

    private String user = "{\n" +
                        "\t\"id\":\"2\",\n" +
                        "\t\"username\":\"erics@atixlabs.com\",\n" +
                        "\t\"email\":\"erics@atsixlabs.com\",\n" +
                        "\t\"name\":\"Eric\",\n" +
                        "\t\"lastName\":\"stoppel\",\n" +
                        "\t\"password\":\"\",\n" +
                        "\t\"newPassword\":\"\",\n" +
                        "\t\"confirmNewPassword\":\"aA1234566\",\n" +
                        "\t\"phone\":\"123456789\",\n" +
                        "\t\"role\":\"ROLE_ADMIN\",\n" +
                        "\t\"type\":\"GLOBAL\"\n" +
                        "}";

    @InjectMocks
    private UsersController usersController;

    private MockMvc mockMvc;

    @Mock
    private UserService service;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }

    @Test
    void findAll() throws Exception{
        Mockito.when(roleRepository.findByCode(com.atixlabs.model.constants.Role.ROLE_ADMIN.role()))
                .thenReturn(Role.builder().code(com.atixlabs.model.constants.Role.ROLE_ADMIN.role()).id(1).build());
        when(userRepository.findAll()).thenReturn(Stream.of(
                new User(376L, "Eric","AA", "username","$2y$12$IAQ4rJsx0Zu5cCphS3jrju7WE4QcUrkF/ZkwZukjMTQiTuMQu0tby", Role.builder().code(com.atixlabs.model.constants.Role.ROLE_ADMIN.role()).id(1).build()),
                new User(375L, "Juan", "BB", "username2","$2y$12$IAQ4rJsx0Zu5cCphS3jrju7WE4QcUrkF/ZkwZukjMTQiTuMQu0tby", Role.builder().code(com.atixlabs.model.constants.Role.ROLE_ADMIN.role()).id(1).build())
        ).collect(Collectors.toList()));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        verify(userRepository).findAll();
    }

    @Test
    void create() throws Exception{
        Mockito.when(userRepository.findAll()).thenReturn(Stream.of(
                new User(376L, "Eric","AA", "username","$2y$12$IAQ4rJsx0Zu5cCphS3jrju7WE4QcUrkF/ZkwZukjMTQiTuMQu0tby", Role.builder().code(com.atixlabs.model.constants.Role.ROLE_ADMIN.role()).id(1).build()),
                new User(375L, "Juan", "BB", "username2","$2y$12$IAQ4rJsx0Zu5cCphS3jrju7WE4QcUrkF/ZkwZukjMTQiTuMQu0tby", Role.builder().code(com.atixlabs.model.constants.Role.ROLE_ADMIN.role()).id(1).build())
        ).collect(Collectors.toList()));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        verify(userRepository).findAll();
    }

    @Test
    void updateNonExistentUser_andExpectToFail() throws Exception{
        when(service.findById(any(Long.class))).thenThrow(UsernameNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .put(UsersController.URL_MAPPING_USERS +"/1")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateExistentUser_andExpectAUser() throws Exception{

        when(service.findById(any(Long.class))).thenReturn(new User());
        when(userRepository.save(any(User.class))).thenReturn(new User());

        mockMvc.perform(MockMvcRequestBuilders
                .put(UsersController.URL_MAPPING_USERS +"/2")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createUserWithInvalidPassword_andExpectToFail() throws Exception{
        String user = "{\n" +
                "\t\"username\":\"erics@atixlabs.com\",\n" +
                "\t\"email\":\"erics@atsixlabs.com\",\n" +
                "\t\"name\":\"Eric\",\n" +
                "\t\"lastName\":\"stoppel\",\n" +
                "\t\"password\":\"\",\n" +
                "\t\"newPassword\":\"\",\n" +
                "\t\"confirmNewPassword\":\"a\",\n" +
                "\t\"phone\":\"123456789\",\n" +
                "\t\"role\":\"ROLE_ADMIN\",\n" +
                "\t\"type\":\"GLOBAL\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(UsersController.URL_MAPPING_USERS)
                .content(user)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createValidUser() throws Exception{
        String user = "{\n" +
                "\t\"username\":\"erics@atixlabs.com\",\n" +
                "\t\"email\":\"erics@atsixlabs.com\",\n" +
                "\t\"name\":\"Eric\",\n" +
                "\t\"lastName\":\"stoppel\",\n" +
                "\t\"password\":\"\",\n" +
                "\t\"newPassword\":\"\",\n" +
                "\t\"confirmNewPassword\":\"Aa123456\",\n" +
                "\t\"phone\":\"123456789\",\n" +
                "\t\"role\":\"ROLE_ADMIN\",\n" +
                "\t\"type\":\"GLOBAL\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(UsersController.URL_MAPPING_USERS)
                .content(user)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}