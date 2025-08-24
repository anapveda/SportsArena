package com.example.Sports_Arena.Service;

import com.example.Sports_Arena.Model.*;
import com.example.Sports_Arena.Repository.SportsArenaRepo;
import com.example.Sports_Arena.Utils.Edge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SportsArenaService {

    @Autowired
    SportsArenaRepo sportsArenaRepo;

    @Autowired
    FindAllCourts findAllCourts;

    public ResponseEntity<?> addSportsArena(SportsArenaWithCourts sportsArenaWithCourts) {

        SportsArena sportsArena = new SportsArena();
        sportsArena.setName(sportsArenaWithCourts.getSportsArena().getName());
        sportsArena.setLatitude(sportsArenaWithCourts.getSportsArena().getLatitude());
        sportsArena.setLongitude(sportsArenaWithCourts.getSportsArena().getLongitude());
        SportsArena existing = sportsArenaRepo.findBySportsArenaName(sportsArena.getName());
        if (existing != null) {
            sportsArena = existing;

        }
        SportsArena savedArena=sportsArenaRepo.save(sportsArena);
        Long arenaId = savedArena.getId();
        if (sportsArenaWithCourts.getCourts() != null && !sportsArenaWithCourts.getCourts().isEmpty()) {
            List<Long> courtIds = sportsArenaWithCourts.getCourts().stream()
                    .map(CourtDTO::getId)
                    .toList();

            AssignCourtsRequest a = new AssignCourtsRequest();
            a.setArenaId(arenaId);   // ✅ now correct
            a.setCourtIds(courtIds);

            System.out.println(a.getArenaId() + " " + a.getCourtIds());

            findAllCourts.assignCourtsToArena(a);
        }

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


    public Map<SportsArena,Double> findNearestSportsArena(LocationDTO userLocationDTO) {
        Map<SportsArena,Double> nearestSportsArena=new HashMap<>();
        List<SportsArena> getAllSportsArena=sportsArenaRepo.findAll();
        Map<String, List<Edge>> graph=AdjacenyMatrix(getAllSportsArena,userLocationDTO);
        Map<String, Double> nearestDistances= dijkstra(graph,"U");
        for( Map.Entry<String, Double> m:nearestDistances.entrySet()){

         if(!m.getKey().equalsIgnoreCase("U")) {
             SportsArena s=sportsArenaRepo.findBySportsArenaName(m.getKey());
             nearestSportsArena.put(s,m.getValue());
         }

        }

        return nearestSportsArena;
    }

    private Map<String, List<Edge>> AdjacenyMatrix(List<SportsArena> getAllSportsArena, LocationDTO userLocationDTO) {
        Map<String, List<Edge>> graph = new HashMap<>();
        List<LocationDTO> locations=new ArrayList<>();
        for(SportsArena s: getAllSportsArena){
            LocationDTO l=new LocationDTO();
            l.setName(s.getName());
            l.setLatitude(s.getLatitude());
            l.setLongitude(s.getLongitude());
            locations.add(l);
        }
        Map<String,Double> userLocationToSportsArena=new HashMap<>();
        List<Edge> weightsFromUserToArena=new ArrayList<>();
        for(int i=0;i< locations.size();i++){
            //String arena="A"+(i+1);
            double distance=haversine(userLocationDTO.getLatitude(), userLocationDTO.getLongitude(),
                    locations.get(i).getLatitude(),locations.get(i).getLongitude());
            userLocationToSportsArena.put(locations.get(i).getName(),distance);
            Edge e=new Edge(locations.get(i).getName(),distance);
            weightsFromUserToArena.add(e);
        }
        graph.put("U",weightsFromUserToArena);
        for(int i=0;i< locations.size();i++){
            //String arena1="A"+(i+1);
            List<Edge> weightsFromArena =new ArrayList<>();
            Edge e=new Edge("U",userLocationToSportsArena.get(locations.get(i).getName()));
            weightsFromArena.add(e);
            for(int j=0;j<locations.size();j++){
            if(i!=j){
                //String arena2="A"+(j+1);
                double distance=haversine(locations.get(i).getLatitude(),locations.get(i).getLongitude(),
                        locations.get(j).getLatitude(),locations.get(j).getLongitude());
                Edge edge=new Edge(locations.get(j).getName(),distance);
                weightsFromArena.add(edge);
            }
            }
            graph.put(locations.get(i).getName(),weightsFromArena);
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

    public static Map<String, Double> dijkstra(Map<String, List<Edge>> graph, String source) {
        // Distance map (initialize to infinity)
        Map<String, Double> distances = new HashMap<>();
        for (String node : graph.keySet()) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(source, 0.0);

        // Min-heap priority queue (node, distance)
        PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );
        pq.add(new AbstractMap.SimpleEntry<>(source, 0.0));

        while (!pq.isEmpty()) {
            String current = pq.poll().getKey();

            for (Edge edge : graph.getOrDefault(current, new ArrayList<>())) {
                double newDist = distances.get(current) + edge.getWeight();
                if (newDist < distances.get(edge.getTarget())) {
                    distances.put(edge.getTarget(), newDist);
                    pq.add(new AbstractMap.SimpleEntry<>(edge.getTarget(), newDist));
                }
            }
        }

        return distances;
    }

}
