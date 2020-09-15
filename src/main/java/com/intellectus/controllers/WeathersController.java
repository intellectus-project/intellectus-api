package com.intellectus.controllers;

import com.intellectus.controllers.model.RingsChartDto;
import com.intellectus.services.ReportService;
import com.intellectus.services.weather.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(WeathersController.URL_MAPPING_WEATHERS)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
public class WeathersController {
    public static final String URL_MAPPING_WEATHERS = "/weathers";

    @Autowired
    public WeathersController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    private WeatherService weatherService;

    @GetMapping()
    public ResponseEntity<?> getRingsChart(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return ResponseEntity.ok().body(weatherService.getDayWeatherInfo(date));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}