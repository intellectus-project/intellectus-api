package com.intellectus.controllers;

import com.intellectus.controllers.model.CallRequestPostDto;
import com.intellectus.controllers.model.CallRequestPatchDto;
import com.intellectus.controllers.model.CallResponseDto;
import com.intellectus.model.configuration.User;
import com.intellectus.security.UserPrincipal;
import com.intellectus.services.CallService;
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
    public ResponseEntity<String> update(@RequestBody @Valid CallRequestPatchDto call, @PathVariable @Min(1) Long id) {
        try {
            callService.update(call, id);
            return ResponseEntity.ok().body("updated");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> fetch(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                                   @AuthenticationPrincipal UserPrincipal principal) {
        try {
            return ResponseEntity.ok().body(callService.fetchByDate(dateFrom, dateTo, principal.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
