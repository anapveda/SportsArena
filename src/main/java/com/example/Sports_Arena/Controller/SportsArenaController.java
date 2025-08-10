package com.example.Sports_Arena.Controller;


import com.example.Sports_Arena.Model.SportsArena;
import com.example.Sports_Arena.Model.SportsArenaWithCourts;
import com.example.Sports_Arena.Service.SportsArenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arena")
public class SportsArenaController {

    @Autowired
    SportsArenaService sportsArenaService;
//fetch based on nearest location
    @GetMapping("/getAll")
    public List<SportsArena> getAllSportsArena(){
        return sportsArenaService.fetchAllSportsArenaDetails();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSportsArena(@RequestBody SportsArena sportsArena){
        return sportsArenaService.addSportsArena(sportsArena);
    }

    @DeleteMapping("/delete/{sportsArenaId}")
    public ResponseEntity<?> deleteSportsArena(@PathVariable Long sportsArenaId){
        return sportsArenaService.deleteSportsArena(sportsArenaId);
    }
    @GetMapping("/{arenaId}/courts")
    public SportsArenaWithCourts getAllCourtdForArena(@PathVariable Long arenaId){
        return sportsArenaService.getAllCourtsForArena(arenaId);
    }



}
