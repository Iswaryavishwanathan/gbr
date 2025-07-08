package com.raison.gbr.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.raison.gbr.Service.SiloService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/silo")
public class Controller {
    
    @Autowired
    private SiloService service;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'][' ']HH:mm");

    @GetMapping("/data")
    public Map<String, List<Map<String, Object>>> getSiloData(
            @RequestParam String siloType,  
            @RequestParam String reportType,  
            @RequestParam String startDate, 
            @RequestParam String endDate) {

        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        return service.getSiloData(siloType, reportType, start, end);
    }
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportSiloData(
            @RequestParam String siloType,
            @RequestParam String reportType,
            @RequestParam String startDateTime,
            @RequestParam String endDateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"); // No seconds
            LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);
    
            byte[] excelData = service.exportSiloDataToExcel(siloType, reportType, start, end);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=silo_report.xlsx");
    
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
}
