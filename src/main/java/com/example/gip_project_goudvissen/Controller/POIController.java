package com.example.gip_project_goudvissen.Controller;

import com.example.gip_project_goudvissen.DTO.POIDTO;
import com.example.gip_project_goudvissen.Entity.POI;
import com.example.gip_project_goudvissen.Service.JwtTokenService;
import com.example.gip_project_goudvissen.Service.POIService;
import com.example.gip_project_goudvissen.Service.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/POI")
public class POIController {

    @Autowired
    private POIService poiService;

    @Autowired
    private JwtTokenService tokenService;


    @PostMapping("{routeId}/create")
    public ResponseEntity<POI> createPOI(@PathVariable Long routeId, @RequestBody POIDTO poidto, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            POI createdPOI = poiService.addPOI(routeId, userId, poidto);
            if (createdPOI != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPOI);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("{id}/updatePoi")
    public ResponseEntity<POI> updatePOI(@PathVariable Long id, @RequestBody POIDTO poidto, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            POI updatedPOI = poiService.updatePOI(id, userId, poidto);
            if (updatedPOI != null) {
                return ResponseEntity.ok(updatedPOI);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("{id}")
    public ResponseEntity<POI> getPOI(@PathVariable Long id, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null) {
            Long userId = tokenService.extractUserId(token);
            POI poi = poiService.findPOIById(id, userId);
            if (poi != null) {
                return ResponseEntity.ok(poi);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<POI>> getAllPOI(){
        List<POI> pois = poiService.getAllPOIs();
        if (pois != null) {
            return ResponseEntity.ok(pois);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/POIByRoute/{routeId}")
    public ResponseEntity<List<POI>> getAllPOIByRoute(@PathVariable Long routeId){
        List<POI> pois = poiService.getAllPOIByRoute(routeId);
        if (pois != null) {
            return ResponseEntity.ok(pois);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deletePOI(@PathVariable Long id, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            poiService.deletePOI(id, userId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/hiddenPOIs")
    public ResponseEntity<List<POI>> getHiddenPOIs(HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            List<POI> hiddenPOIs = poiService.getHiddenPOIs(userId);
            return ResponseEntity.ok(hiddenPOIs);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}/setVisible/{visible}")
    public ResponseEntity<POI> setPOIVisibility(@PathVariable Long id, @PathVariable Boolean visible, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            POI updatedPOI = poiService.setPOIVisibility(id, visible, userId);
            if (updatedPOI != null){
                return ResponseEntity.ok(updatedPOI);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/withinRange/{radiusInKilometers}")
    public ResponseEntity<List<POI>> getAllPOIInRange(@PathVariable int radiusInKilometers, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null) {
            Long userId = tokenService.extractUserId(token);
            List<POI> poisInRange = poiService.getAllPOIInRange(userId, radiusInKilometers);
            return ResponseEntity.ok(poisInRange);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
