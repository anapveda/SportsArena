package com.example.Sports_Arena.Service;

import com.example.Sports_Arena.Model.LocationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8080")
public interface UserLocation {
    @GetMapping("/users/{id}/location")
    public ResponseEntity<LocationDTO> getUserLocation(@PathVariable long id);
    //Map<String, Double> getUserLocation(@PathVariable Long id);

}
