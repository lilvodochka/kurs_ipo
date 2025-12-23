package com.example.ticketsearchapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ticketsearchapp.dto.ArtistDTO;
import com.example.ticketsearchapp.dto.EventDTO;
import com.example.ticketsearchapp.dto.VenueDTO;
import com.example.ticketsearchapp.entity.Artist;
import com.example.ticketsearchapp.entity.Event;
import com.example.ticketsearchapp.entity.Venue;
import com.example.ticketsearchapp.repository.ArtistRepository;
import com.example.ticketsearchapp.repository.EventRepository;
import com.example.ticketsearchapp.repository.VenueRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private VenueRepository venueRepository;

    public List<EventDTO> searchEvents(String criteria) {
        List<Event> events = eventRepository.searchPublishedEvents(criteria != null ? criteria : "");
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public EventDTO getEventDetails(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        return convertToDTO(event);
    }

    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setPosterUrl(event.getPosterUrl());
        dto.setStatus(event.getStatus());

        // Загрузка деталей
        Artist artist = artistRepository.findById(event.getArtistId()).orElse(null);
        if (artist != null) {
            ArtistDTO artistDTO = new ArtistDTO();
            artistDTO.setId(artist.getId());
            artistDTO.setName(artist.getName());
            artistDTO.setGenre(artist.getGenre());
            artistDTO.setDescription(artist.getDescription());
            artistDTO.setPhotoUrl(artist.getPhotoUrl());
            dto.setArtist(artistDTO);
        }

        Venue venue = venueRepository.findById(event.getVenueId()).orElse(null);
        if (venue != null) {
            VenueDTO venueDTO = new VenueDTO();
            venueDTO.setId(venue.getId());
            venueDTO.setName(venue.getName());
            venueDTO.setAddress(venue.getAddress());
            venueDTO.setCity(venue.getCity());
            venueDTO.setTotalCapacity(venue.getTotalCapacity());
            venueDTO.setDescription(venue.getDescription());
            dto.setVenue(venueDTO);
        }

        return dto;
    }
}