package com.example.Sports_Arena.Service;

import com.example.Sports_Arena.Model.CourtDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "COURT-SERVICE", url = "http://localhost:8082")
public interface FindAllCourts {

    @GetMapping("/court/arena/{arenaId}")
    List<CourtDTO> getAvailableCourts(@PathVariable("arenaId") Long arenaId);
}
