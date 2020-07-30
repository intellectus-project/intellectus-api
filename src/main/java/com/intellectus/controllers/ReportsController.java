package com.intellectus.controllers;


import com.google.common.collect.Lists;
import com.intellectus.controllers.model.RingsChartDto;
import com.intellectus.model.configuration.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping(ReportsController.URL_MAPPING_REPORTS)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
public class ReportsController {
    public static final String URL_MAPPING_REPORTS = "/reports";

    @GetMapping
    public ResponseEntity<RingsChartDto> getRingsChart()
    {
        RingsChartDto ringsChartDto = RingsChartDto.builder().build();
        return ResponseEntity.ok().body(ringsChartDto);
    }
}
