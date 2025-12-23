package com.example.ticketsearchapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.ticketsearchapp.dto.EventDTO;
import com.example.ticketsearchapp.service.SearchService;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.example.ticketsearchapp.service.ReportService;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private SearchService searchService;
    
    @Autowired
    private ReportService reportService;

    @GetMapping("/search")
    public List<EventDTO> searchEvents(@RequestParam(required = false) String criteria) {
        return searchService.searchEvents(criteria);
    }

    @GetMapping("/events/{id}")
    public EventDTO getEventDetails(@PathVariable Long id) {
        return searchService.getEventDetails(id);
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateReport(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime start = LocalDateTime.parse(startDateStr, formatter);
            LocalDateTime end = LocalDateTime.parse(endDateStr, formatter);

            ByteArrayInputStream in = reportService.generateEventsReport(start, end);
            byte[] bytes = in.readAllBytes();
            in.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=events_report.xlsx");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400)
                    .body(("Ошибка генерации отчёта: " + e.getMessage()).getBytes());
        }
    }
}