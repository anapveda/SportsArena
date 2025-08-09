package com.example.Sports_Arena.Service;

import com.example.Sports_Arena.Model.SportsArena;
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
    public ResponseEntity<?> addSportsArena(SportsArena sportsArena) {
        SportsArena s=new SportsArena();
        s.setName(sportsArena.getName());
        s.setLatitude(sportsArena.getLatitude());
        s.setLongitude(sportsArena.getLongitude());
        sportsArenaRepo.save(s);
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
}
