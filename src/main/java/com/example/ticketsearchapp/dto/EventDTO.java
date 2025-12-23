package com.example.ticketsearchapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String posterUrl;
    private String status;

    // Детали
    private ArtistDTO artist;
    private VenueDTO venue;
}
