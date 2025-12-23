package com.example.ticketsearchapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.ticketsearchapp.entity.Event;

import java.util.List;
import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e " +
           "JOIN Artist a ON e.artistId = a.id " +
           "JOIN Venue v ON e.venueId = v.id " +
           "WHERE e.status = 'published' AND " +
           "(LOWER(e.title) LIKE LOWER(concat('%', :criteria, '%')) OR " +
           "LOWER(a.name) LIKE LOWER(concat('%', :criteria, '%')) OR " +
           "LOWER(v.city) LIKE LOWER(concat('%', :criteria, '%')))")
    List<Event> searchPublishedEvents(@Param("criteria") String criteria);

    List<Event> findByEventDateBetween(LocalDateTime start, LocalDateTime end);
}