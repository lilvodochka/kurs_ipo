package com.example.ticketsearchapp.dto;

import lombok.Data;

@Data
public class ArtistDTO {
    private Long id;
    private String name;
    private String genre;
    private String description;
    private String photoUrl;
}