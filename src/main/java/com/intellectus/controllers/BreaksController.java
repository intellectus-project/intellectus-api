package com.intellectus.controllers;

import com.intellectus.controllers.model.CallRequestPostDto;
import com.intellectus.controllers.model.CallResponseDto;
import com.intellectus.model.Call;
import com.intellectus.model.configuration.User;
import com.intellectus.security.UserPrincipal;
import com.intellectus.services.BreakService;
import com.intellectus.services.CallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(BreaksController.URL_MAPPING_BREAKS)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PATCH})
public class BreaksController {

    public static final String URL_MAPPING_BREAKS = "/breaks";
    private BreakService breakService;
    private CallService callService;

    @Autowired
    public BreaksController(BreakService breakService, CallService callService) {
        this.breakService = breakService;
        this.callService = callService;
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<?> create(@RequestParam @Min(1) Long callId) {
        try {
            Optional<Call> call = callService.findById(callId);
            if(!call.isPresent())
                return ResponseEntity.badRequest().body("Call not found");
            breakService.create(call.get());
            return ResponseEntity.ok().body("created");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
