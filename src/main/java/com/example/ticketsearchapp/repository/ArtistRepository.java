package com.example.ticketsearchapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ticketsearchapp.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}