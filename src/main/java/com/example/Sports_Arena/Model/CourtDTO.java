package com.example.Sports_Arena.Model;

import lombok.Data;

@Data
public class CourtDTO {
    private Long Id;
    private String type; // e.g. Single, Double
    private Double price;
    private Boolean isAvailable;
    private  Long sportsArenaId;
}
