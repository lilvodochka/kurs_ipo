package com.example.ticketsearchapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ticketsearchapp.entity.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
