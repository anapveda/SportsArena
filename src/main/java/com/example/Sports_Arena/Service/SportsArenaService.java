package com.example.Sports_Arena.Service;

import com.example.Sports_Arena.Model.CourtDTO;
import com.example.Sports_Arena.Model.LocationDTO;
import com.example.Sports_Arena.Model.SportsArena;
import com.example.Sports_Arena.Model.SportsArenaWithCourts;
import com.example.Sports_Arena.Repository.SportsArenaRepo;
import com.example.Sports_Arena.Utils.Edge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public List<SportsArena> findNearestSportsArena(LocationDTO userLocationDTO) {
        List<SportsArena> nearestSportsArena=new ArrayList<>();
        List<SportsArena> getAllSportsArena=sportsArenaRepo.findAll();
        Map<String, List<Edge>> graph=AdjacenyMatrix(getAllSportsArena,userLocationDTO);
        System.out.println(graph);
        return nearestSportsArena;
    }

    private Map<String, List<Edge>> AdjacenyMatrix(List<SportsArena> getAllSportsArena, LocationDTO userLocationDTO) {
        Map<String, List<Edge>> graph = new HashMap<>();
        List<LocationDTO> locations=new ArrayList<>();
        for(SportsArena s: getAllSportsArena){
            LocationDTO l=new LocationDTO();
            l.setLatitude(s.getLatitude());
            l.setLongitude(s.getLongitude());
            locations.add(l);
        }
        Map<String,Double> userLocationToSportsArena=new HashMap<>();
        List<Edge> weightsFromUserToArena=new ArrayList<>();
        for(int i=0;i< locations.size();i++){
            String arena="A"+(i+1);
            double distance=haversine(userLocationDTO.getLatitude(), userLocationDTO.getLongitude(),
                    locations.get(i).getLatitude(),locations.get(i).getLongitude());
            userLocationToSportsArena.put(arena,distance);
            Edge e=new Edge(arena,distance);
            weightsFromUserToArena.add(e);
        }
        graph.put("U",weightsFromUserToArena);
        for(int i=0;i< locations.size();i++){
            String arena1="A"+(i+1);
            List<Edge> weightsFromArena =new ArrayList<>();
            Edge e=new Edge("U",userLocationToSportsArena.get(arena1));
            weightsFromArena.add(e);
            for(int j=0;j<locations.size();j++){
            if(i!=j){
                String arena2="A"+(j+1);
                double distance=haversine(locations.get(i).getLatitude(),locations.get(i).getLongitude(),
                        locations.get(j).getLatitude(),locations.get(j).getLongitude());
                Edge edge=new Edge(arena2,distance);
                weightsFromArena.add(edge);
            }
            }
            graph.put(arena1,weightsFromArena);
        }
        return graph;

    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in KM
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

}
