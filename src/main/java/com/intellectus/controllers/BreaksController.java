package com.intellectus.controllers;

import com.intellectus.controllers.model.CallRequestPostDto;
import com.intellectus.controllers.model.CallResponseDto;
import com.intellectus.model.Break;
import com.intellectus.model.Call;
import com.intellectus.model.configuration.User;
import com.intellectus.security.UserPrincipal;
import com.intellectus.services.BreakService;
import com.intellectus.services.CallBreakService;
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
@RequestMapping(BreaksController.URL_MAPPING_BREAKS)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PATCH})
public class BreaksController {

    public static final String URL_MAPPING_BREAKS = "/breaks";
    private BreakService breakService;
    private CallService callService;
    private UserService userService;
    private CallBreakService callBreakService;


    @Autowired
    public BreaksController(BreakService breakService, CallService callService, UserService userService, CallBreakService callBreakService) {
        this.breakService = breakService;
        this.callService = callService;
        this.userService = userService;
        this.callBreakService = callBreakService;
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<?> create(@RequestParam @Min(1) Long callId, @RequestParam Optional<Integer> minutesDuration) {
        try {
            Optional<Call> call = callService.findById(callId);
            if(!call.isPresent())
                return ResponseEntity.badRequest().body("Call not found");
            breakService.create(call.get(), defaultDuration(minutesDuration));
            return ResponseEntity.ok().body("created");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @PostMapping("/createBySupervisor")
    public ResponseEntity<?> createBySupervisor(@RequestParam Optional<Integer> minutesDuration, @RequestParam Long operatorId) {
        try {
            User user = userService.findById(operatorId);
            Optional<Break> breakOpt = callBreakService.lastCallBreak(user);
            if(breakOpt.isPresent())
                return ResponseEntity.badRequest().body("Break already assigned");
            callBreakService.createBreakBySupervisor(user, defaultDuration(minutesDuration));
            return ResponseEntity.ok().body("created");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERVISOR', 'ROLE_OPERATOR')")
    @GetMapping
    public ResponseEntity<?> fetchByUserAndDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                                                @RequestParam Optional<Long> operatorId,
                                                @AuthenticationPrincipal UserPrincipal operator) {
        try {
            User user = operatorId.isPresent() ? userService.findById(operatorId.get()) : userService.findById(operator.getId());
            return ResponseEntity.ok().body(breakService.fetchByUserAndDate(dateFrom, dateTo, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @GetMapping("/lastCall")
    public ResponseEntity<?> lastCallBreak(@AuthenticationPrincipal UserPrincipal operator) {
        try {
            User user = new User(operator.getId());
            return ResponseEntity.ok().body(callBreakService.lastCallBreak(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Integer defaultDuration(Optional<Integer> minutesDuration) {
        return minutesDuration.orElse(10);
    }
}
