package com.example.gip_project_goudvissen.Service;

import com.example.gip_project_goudvissen.DTO.POIDTO;
import com.example.gip_project_goudvissen.Entity.*;
import com.example.gip_project_goudvissen.Repository.POIRepository;
import com.example.gip_project_goudvissen.Repository.RouteRepository;
import com.example.gip_project_goudvissen.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class POIService {

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRepository userRepository;

    public POI addPOI(Long routeId, Long userId, POIDTO poidto){
        POI poi = new POI();
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        poi.setRoute(route);
        poi.setCreatedByUser(user);
        poi.setName(poidto.getName());
        poi.setLocation(poidto.getLocation());
        poi.setType(poidto.getType());
        poi.setVisible(false);
        return poiRepository.save(poi);
    }

    public POI updatePOI(Long id, Long userId, POIDTO poidto){
        POI foundPoi = poiRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "POI not found"));
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (Objects.equals(foundPoi.getCreatedByUser(), foundUser)) {
            foundPoi.setName(poidto.getName());
            foundPoi.setLocation(poidto.getLocation());
            foundPoi.setType(poidto.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to edit this poi");
        }
        return poiRepository.save(foundPoi);
    }

    public POI findPOIById(Long id, Long userId){
        POI foundPoi = poiRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "POI not found"));
        if (Objects.equals(foundPoi.getCreatedByUser().getId(), userId)){
            return foundPoi;
        } else {
            POI poi = poiRepository.findByIdAndVisibleTrue(id);
            if (poi == null){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to see this poi");
            }
            return poi;
        }
    }

    public List<POI> getAllPOIs(){
        return poiRepository.findByVisibleTrue();
    }

    public List<POI> getAllPOIByRoute(Long routeId) {
        return poiRepository.findByRouteIdAndVisibleTrue(routeId);
    }

    public void deletePOI(Long id, Long userId){
        POI poi = poiRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "POI not found"));
        if (Objects.equals(poi.getCreatedByUser().getId(), userId)){
            poiRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to delete this POI");
        }
    }

    public List<POI> getHiddenPOIs(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (Role.ROLE_ADMIN.equals(role.getName())) {
                return poiRepository.findByVisibleFalse();
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to view these POI");
            }
        }
        return user.getCreatedPOIs();
    }

    public POI setPOIVisibility(Long id, Boolean visible, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        POI foundPoi = poiRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "POI not found"));
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (Role.ROLE_ADMIN.equals(role.getName())) {
                foundPoi.setVisible(visible);
                return poiRepository.save(foundPoi);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to update this POI");
            }
        }
        return foundPoi;
    }

    public List<POI> getAllPOIInRange(Long userId, int radiusInKilometers) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Coordinate userLocation = user.getLocation();
        List<POI> allPOIs = poiRepository.findByVisibleTrue();

        return allPOIs.stream()
                .filter(poi -> isWithinRange(userLocation, poi.getLocation(), radiusInKilometers))
                .collect(Collectors.toList());
    }

    private boolean isWithinRange(Coordinate userLocation, Coordinate poiLocation, int radiusInKilometers) {
        int distance = (int) Math.round(calculateHaversineDistance(userLocation, poiLocation));
        return distance <= radiusInKilometers;
    }

    private double calculateHaversineDistance(Coordinate location1, Coordinate location2) {
        double earthRadius = 6371; // Kilometers

        double lat1 = Math.toRadians(location1.getY());
        double lon1 = Math.toRadians(location1.getX());
        double lat2 = Math.toRadians(location2.getY());
        double lon2 = Math.toRadians(location2.getX());

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

}
