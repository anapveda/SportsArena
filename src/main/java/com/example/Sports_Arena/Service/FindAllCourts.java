package com.example.Sports_Arena.Service;

import com.example.Sports_Arena.Authentication.FeignClientConfig;
import com.example.Sports_Arena.Model.CourtDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "COURT-SERVICE",configuration = FeignClientConfig.class)
public interface FindAllCourts {

    @GetMapping("/courts/{arenaId}")
    List<CourtDTO> getAvailableCourts(@PathVariable("arenaId") Long arenaId);
}
