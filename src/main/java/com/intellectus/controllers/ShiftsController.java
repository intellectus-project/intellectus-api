package com.intellectus.controllers;

import com.google.common.collect.Lists;
import com.intellectus.model.Shift;
import com.intellectus.model.configuration.Role;
import com.intellectus.repositories.RoleRepository;
import com.intellectus.services.ShiftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Slf4j
@Validated
@RequestMapping(ShiftsController.URL_MAPPING)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
public class ShiftsController {
    public static final String URL_MAPPING = "/shifts";

    private final ShiftService service;

    @Autowired
    public ShiftsController(ShiftService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public Collection<Shift> findAll() {
        return Lists.newArrayList(service.findAll());
    }
}
