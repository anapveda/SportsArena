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
import java.util.Map;

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
    public ResponseEntity<?> addSportsArena(@RequestBody SportsArenaWithCourts sportsArenaWithCourts){
        return sportsArenaService.addSportsArena(sportsArenaWithCourts);
    }



    @DeleteMapping("/delete/{sportsArenaId}")
    public ResponseEntity<?> deleteSportsArena(@PathVariable Long sportsArenaId){
        return sportsArenaService.deleteSportsArena(sportsArenaId);
    }
    @GetMapping("/{arenaId}")
    public SportsArenaWithCourts getAllCourtdForArena(@PathVariable Long arenaId){
        return sportsArenaService.getAllCourtsForArena(arenaId);
    }
    @GetMapping("/user/location")
    public Map<SportsArena,Double> getNearestSportsArena() {
        LocationDTO userLocationDTO= userLocation.getUserLocation().getBody();
        Map<SportsArena,Double> nearestSportsArena=sportsArenaService.findNearestSportsArena(userLocationDTO);
        for(Map.Entry<SportsArena,Double> m: nearestSportsArena.entrySet()){
            System.out.println(m.getKey().getName()+" "+m.getValue());
        }
        return nearestSportsArena;
    }



}
