package com.example.Sports_Arena.Model;

public class Court {
    private Long id;
    private String courtNumber;
    private String type; // e.g. Single, Double
    private Double price;
    private Boolean isAvailable;
    private  Long sportsArenaId; // reference to Auditorium microservice
}
