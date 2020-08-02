package com.intellectus.controllers;


import com.intellectus.controllers.model.RingsChartDto;
import com.intellectus.services.CallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@Slf4j
@RequestMapping(ReportsController.URL_MAPPING_REPORTS)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
public class ReportsController {
    public static final String URL_MAPPING_REPORTS = "/reports";

    @Autowired
    public ReportsController(CallService callService){
        this.callService = callService;
    }

    private CallService callService;

    @GetMapping("/ringsChart")
    public ResponseEntity<RingsChartDto> getRingsChart(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo)
    {
        return ResponseEntity.ok().body(callService.getRingsChart(dateFrom, dateTo));
    }

}
