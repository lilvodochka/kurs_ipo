package com.example.ticketsearchapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "artists")
@Data
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String genre;

    private String description;

    @Column(name = "photo_url")
    private String photoUrl;
}
