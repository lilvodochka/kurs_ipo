package com.example.ticketsearchapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "venues")
@Data
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    private String city;

    @Column(name = "total_capacity")
    private Integer totalCapacity;

    private String description;
}
