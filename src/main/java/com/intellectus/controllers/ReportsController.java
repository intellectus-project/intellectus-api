package com.intellectus.controllers;


import com.intellectus.controllers.model.BarsChartDto;
import com.intellectus.controllers.model.RingsChartDto;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Role;
import com.intellectus.services.CallService;
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
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                                                       @RequestParam Optional<Integer> operatorId)
    {
        return ResponseEntity.ok().body(callService.getRingsChart(dateFrom, dateTo));
    }

    @GetMapping("/barsChart")
    public ResponseEntity<?> getBarsChart(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                                                     @RequestParam Optional<Long> operatorId)
    {
        try {

        }catch (UsernameNotFoundException usernameNotFoundException){
            return ResponseEntity.badRequest().body(String.format("User with id %s not found", operatorId.get()));
        }catch (RuntimeException rex){
            return ResponseEntity.badRequest().body(rex.getMessage());
        }


        return ResponseEntity.ok().body(callService.getBarsChart(dateFrom, dateTo, operatorId));
    }

}
