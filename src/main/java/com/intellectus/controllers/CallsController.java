package com.intellectus.controllers;

import com.intellectus.controllers.model.CallRequestPostDto;
import com.intellectus.controllers.model.CallRequestPatchDto;
import com.intellectus.controllers.model.CallResponseDto;
import com.intellectus.controllers.model.CallResponsePatchDto;
import com.intellectus.model.configuration.User;
import com.intellectus.security.UserPrincipal;
import com.intellectus.services.CallService;
import com.intellectus.services.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(CallsController.URL_MAPPING_CALLS)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PATCH})
public class CallsController {

    public static final String URL_MAPPING_CALLS = "/calls";
    private CallService callService;

    @Autowired
    private UserService userService;

    @Autowired
    public CallsController(CallService callService){
        this.callService = callService;
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal UserPrincipal operator, @RequestBody @Valid CallRequestPostDto call) {
        try {
            User user = new User(operator.getId());
            Long id = callService.create(user, call);
            return ResponseEntity.ok().body(new CallResponseDto(id));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid CallRequestPatchDto call, @PathVariable @Min(1) Long id) {
        try {
            Boolean breakAssigned = callService.update(call, id);
            return ResponseEntity.ok().body(CallResponsePatchDto.builder().breakAssigned(breakAssigned).build());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> fetch(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                                   @AuthenticationPrincipal UserPrincipal principal) {
        try {
            return ResponseEntity.ok().body(callService.fetchByDateAndSupervisor(dateFrom, dateTo, principal.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR') || hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable @Min(1) Long id) {
        try {
            return ResponseEntity.ok().body(callService.findByIdWithInfo(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/byOperator")
    public ResponseEntity<?> byOperator(@RequestParam Optional<Long> id,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        try {
            User user = id.isPresent() ? userService.findById(id.get()) : userService.findById(principal.getId());
            return ResponseEntity.ok().body(callService.fetchByDateAndOperator(date, user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
