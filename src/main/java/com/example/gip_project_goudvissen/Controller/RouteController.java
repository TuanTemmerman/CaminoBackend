package com.example.gip_project_goudvissen.Controller;

import com.example.gip_project_goudvissen.Entity.Coordinate;
import com.example.gip_project_goudvissen.Service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/getRoute")
    public ResponseEntity<String> getRoute(@RequestParam String startLocation, @RequestParam String endLocation) {
        String apiUrl = "https://api.openrouteservice.org/v2/directions/driving-car?api_key={apiKey}&start={start}&end={end}";

        apiUrl = apiUrl.replace("{apiKey}", "5b3ce3597851110001cf62484546086ed9504800b9a51eee82f7cf93")
                .replace("{start}", startLocation)
                .replace("{end}", endLocation);


        RestTemplate restTemplate = new RestTemplate();
        String routeData = restTemplate.getForObject(apiUrl, String.class);
        return ResponseEntity.ok(routeData);
    }

    @GetMapping("/getCoordinates")
    public ResponseEntity<String> getCoordinates(@RequestParam String location){
        String apiUrl = "https://api.openrouteservice.org/geocode/search?api_key={apiKey}&text={text}";

        apiUrl = apiUrl.replace("{apiKey}", "5b3ce3597851110001cf62484546086ed9504800b9a51eee82f7cf93")
                .replace("{text}", location);

        RestTemplate restTemplate = new RestTemplate();
        String string = restTemplate.getForObject(apiUrl, String.class);

        return ResponseEntity.ok(string);
    }

    @PostMapping("/getCaminoRoute")
    public ResponseEntity<String> getCaminoRouteFromDatabase() {
        try {
            Coordinate startCoordinate = routeService.getStartCoordinate();
            Coordinate endCoordinate = routeService.getEndCoordinate();
            List<Coordinate> waypointCoordinates = routeService.getWaypointCoordinates();

            Map<String, List<List<Double>>> requestBody = new HashMap<>();
            requestBody.put("coordinates", new ArrayList<>());

            requestBody.get("coordinates").add(List.of(startCoordinate.getX(), startCoordinate.getY()));

            requestBody.get("coordinates").addAll(waypointCoordinates.stream()
                    .map(coordinate -> List.of(coordinate.getX(), coordinate.getY()))
                    .collect(Collectors.toList()));

            requestBody.get("coordinates").add(List.of(endCoordinate.getX(), endCoordinate.getY()));

            String coordinates = new ObjectMapper().writeValueAsString(requestBody);

            String apiUrl = "https://api.openrouteservice.org/v2/directions/driving-car/geojson";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "5b3ce3597851110001cf62484546086ed9504800b9a51eee82f7cf93");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(coordinates, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            return ResponseEntity.ok(responseEntity.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching coordinates from the database.");
        }
    }
}
