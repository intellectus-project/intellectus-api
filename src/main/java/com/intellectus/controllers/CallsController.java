package com.intellectus.controllers;

import com.intellectus.controllers.model.CallDto;
import com.intellectus.model.configuration.User;
import com.intellectus.security.UserPrincipal;
import com.intellectus.services.call.CallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(CallsController.URL_MAPPING_CALLS)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PATCH})
public class CallsController {

    public static final String URL_MAPPING_CALLS = "/calls";
    private CallService callService;

    @Autowired
    public CallsController(CallService callService){
        this.callService = callService;
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<String> create(@AuthenticationPrincipal UserPrincipal operator, @RequestBody @Valid CallDto call) {
        try {
            User user = new User(operator.getId());
            callService.create(user, call);
            return ResponseEntity.ok().body("created");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
