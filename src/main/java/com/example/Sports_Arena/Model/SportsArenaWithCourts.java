package com.example.Sports_Arena.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class SportsArenaWithCourts {
    private SportsArena sportsArena;
    private List<CourtDTO> courts;


}
