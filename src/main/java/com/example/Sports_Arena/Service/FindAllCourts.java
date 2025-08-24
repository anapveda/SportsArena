package com.example.Sports_Arena.Service;

import com.example.Sports_Arena.Authentication.FeignClientConfig;
import com.example.Sports_Arena.Model.AssignCourtsRequest;
import com.example.Sports_Arena.Model.CourtDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "COURT-SERVICE",configuration = FeignClientConfig.class)
public interface FindAllCourts {

    @GetMapping("/courts/{arenaId}")
    List<CourtDTO> getAvailableCourts(@PathVariable("arenaId") Long arenaId);

    @PutMapping("/courts/assign")
    public void assignCourtsToArena(@RequestBody AssignCourtsRequest request);
}
