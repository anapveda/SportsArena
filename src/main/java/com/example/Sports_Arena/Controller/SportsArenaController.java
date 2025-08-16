package com.example.Sports_Arena.Controller;


import com.example.Sports_Arena.Model.LocationDTO;
import com.example.Sports_Arena.Model.SportsArena;
import com.example.Sports_Arena.Model.SportsArenaWithCourts;
import com.example.Sports_Arena.Service.SportsArenaService;
import com.example.Sports_Arena.Service.UserLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arena")
public class SportsArenaController {

    @Autowired
    SportsArenaService sportsArenaService;

    @Autowired
    UserLocation userLocation;
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
    @GetMapping("/user/location")
    public List<SportsArena> getNearestSportsArena() {
        LocationDTO userLocationDTO= userLocation.getUserLocation().getBody();
        List<SportsArena> nearestSportsArena=sportsArenaService.findNearestSportsArena(userLocationDTO);
        return nearestSportsArena;
    }



}
