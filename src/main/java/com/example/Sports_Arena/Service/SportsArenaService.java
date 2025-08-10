package com.example.Sports_Arena.Service;

import com.example.Sports_Arena.Model.CourtDTO;
import com.example.Sports_Arena.Model.SportsArena;
import com.example.Sports_Arena.Model.SportsArenaWithCourts;
import com.example.Sports_Arena.Repository.SportsArenaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SportsArenaService {

    @Autowired
    SportsArenaRepo sportsArenaRepo;

    @Autowired
    FindAllCourts findAllCourts;

    public ResponseEntity<?> addSportsArena(SportsArena sportsArena) {

        sportsArenaRepo.save(sportsArena);
        return new ResponseEntity<>("Sucessfully added Sports Arena", HttpStatus.OK);
    }


    public ResponseEntity<?> deleteSportsArena(Long sportsArenaId) {
        if (sportsArenaRepo.deleteSportsArenaById(sportsArenaId) >= 0) {
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return new ResponseEntity("Court Number doesnot exist", HttpStatus.FORBIDDEN);
    }

    public List<SportsArena> fetchAllSportsArenaDetails() {
         if(sportsArenaRepo.findAll().size()!=0){
             return  sportsArenaRepo.findAll();
         }
         return new ArrayList<>();
    }

    public SportsArenaWithCourts getAllCourtsForArena(Long arenaId) {
        SportsArena sportsArena=sportsArenaRepo.findById(arenaId).orElseThrow(() -> new RuntimeException("sports arena not found"));

        List<CourtDTO> courts = findAllCourts.getAvailableCourts(arenaId);
        SportsArenaWithCourts response = new SportsArenaWithCourts();
        response.setSportsArena(sportsArena);response.setCourts(courts);

        return response;
    }
}
