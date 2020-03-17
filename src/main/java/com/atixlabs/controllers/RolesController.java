package com.atixlabs.controllers;

import com.atixlabs.model.configuration.Role;
import com.atixlabs.repositories.RoleRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Slf4j
@Validated
@RequestMapping(RolesController.URL_MAPPING)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class RolesController {

    public static final String URL_MAPPING = "/roles";

    private final RoleRepository repository;

    public RolesController(RoleRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public Collection<Role> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

}
