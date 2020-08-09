package com.intellectus.controllers;

import com.intellectus.controllers.model.CallRequestPostDto;
import com.intellectus.controllers.model.CallResponseDto;
import com.intellectus.model.configuration.User;
import com.intellectus.security.UserPrincipal;
import com.intellectus.services.CallService;
import com.intellectus.services.newsEvent.NewsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping(NewsEventsController.URL_MAPPING_NEWS_EVENTS)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
public class NewsEventsController {

    public static final String URL_MAPPING_NEWS_EVENTS = "/newsEvents";
    private NewsEventService newsEventService;

    @Autowired
    public NewsEventsController(NewsEventService newsEventService) {
        this.newsEventService = newsEventService;

    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping
    public ResponseEntity<?> create(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        try {
            return ResponseEntity.ok().body(newsEventService.fetchByDate(dateFrom, dateTo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
