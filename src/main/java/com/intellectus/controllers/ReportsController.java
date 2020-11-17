package com.intellectus.controllers;


import com.intellectus.controllers.model.BarsChartDto;
import com.intellectus.controllers.model.RingsChartDto;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Role;
import com.intellectus.security.UserPrincipal;
import com.intellectus.services.CallService;
import com.intellectus.services.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
@RequestMapping(ReportsController.URL_MAPPING_REPORTS)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
public class ReportsController {
    public static final String URL_MAPPING_REPORTS = "/reports";

    @Autowired
    public ReportsController(ReportService reportService){
        this.reportService = reportService;
    }

    private ReportService reportService;

    @GetMapping("/ringsChart")
    public ResponseEntity<?> getRingsChart(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                                                       @RequestParam Optional<Long> operatorId)
    {
        RingsChartDto ringsChartDto;
        try {
            ringsChartDto = reportService.getRingsChart(dateFrom, dateTo, operatorId);
        }catch (UsernameNotFoundException usernameNotFoundException){
            return ResponseEntity.badRequest().body(String.format("User with id %s not found", operatorId.get()));
        }catch (RuntimeException rex){
            return ResponseEntity.badRequest().body(rex.getMessage());
        }


        return ResponseEntity.ok().body(ringsChartDto);
    }

    @GetMapping("/barsChart")
    public ResponseEntity<?> getBarsChart(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                                                     @RequestParam Optional<Long> operatorId,
                                                    @AuthenticationPrincipal UserPrincipal supervisor)
    {
        List<BarsChartDto> barsChartDtoList;
        try {
            barsChartDtoList = reportService.getBarsChart(dateFrom, dateTo, operatorId, supervisor.getId());
        }catch (UsernameNotFoundException usernameNotFoundException){
            return ResponseEntity.badRequest().body(String.format("User with id %s not found", operatorId.get()));
        }catch (RuntimeException rex){
            return ResponseEntity.badRequest().body(rex.getMessage());
        }
        return ResponseEntity.ok().body(barsChartDtoList);
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/barsChartByOperators")
    public ResponseEntity<?> getBarsChartByOperators(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> date,
                                          @AuthenticationPrincipal UserPrincipal supervisor)
    {
        List<BarsChartDto> barsChartDtoList;
        try {
            LocalDate actualDate = date.orElse(LocalDate.now());
            barsChartDtoList = reportService.getBarsChartByOperators(actualDate, supervisor.getId());
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(barsChartDtoList);
    }

}
