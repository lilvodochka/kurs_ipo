package com.example.ticketsearchapp.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ticketsearchapp.entity.Artist;
import com.example.ticketsearchapp.entity.Event;
import com.example.ticketsearchapp.entity.Venue;
import com.example.ticketsearchapp.repository.ArtistRepository;
import com.example.ticketsearchapp.repository.EventRepository;
import com.example.ticketsearchapp.repository.VenueRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private VenueRepository venueRepository;

    public ByteArrayInputStream generateEventsReport(LocalDateTime startDate, LocalDateTime endDate) {
        // Получаем события за период
        List<Event> events = eventRepository.findByEventDateBetween(startDate, endDate);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Список мероприятий");

            // Стили для заголовка
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Заголовок таблицы
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Название", "Дата и время", "Артист", "Площадка", "Город", "Статус"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Данные
            int rowNum = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            for (Event event : events) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(event.getId());
                row.createCell(1).setCellValue(event.getTitle());
                row.createCell(2).setCellValue(event.getEventDate().format(formatter));

                Artist artist = artistRepository.findById(event.getArtistId()).orElse(null);
                row.createCell(3).setCellValue(artist != null ? artist.getName() : "Неизвестно");

                Venue venue = venueRepository.findById(event.getVenueId()).orElse(null);
                row.createCell(4).setCellValue(venue != null ? venue.getName() : "Неизвестно");
                row.createCell(5).setCellValue(venue != null ? venue.getCity() : "Неизвестно");

                row.createCell(6).setCellValue(event.getStatus());
            }

            // Авто-размер колонок
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка генерации отчёта: " + e.getMessage());
        }
    }
}