package com.example.gip_project_goudvissen.Controller;

import com.example.gip_project_goudvissen.DTO.EmergencyContactDTO;
import com.example.gip_project_goudvissen.Entity.EmergencyContact;
import com.example.gip_project_goudvissen.Service.EmergencyContactService;
import com.example.gip_project_goudvissen.Service.JwtTokenService;
import com.example.gip_project_goudvissen.Service.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/emergencyContact")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactService emergencyContactService;

    @Autowired
    private JwtTokenService tokenService;

    @PostMapping("/addExistingUser/{contactUserId}")
    public ResponseEntity<EmergencyContact> addExistingUserAsEmergencyContact(@PathVariable Long contactUserId, @RequestParam String customName, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            EmergencyContact createdEmergencyContact = emergencyContactService.addExistingUserAsEmergencyContact(userId, contactUserId, customName);
            if (createdEmergencyContact != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(createdEmergencyContact);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<EmergencyContact> addNewEmergencyContact(@RequestBody EmergencyContactDTO emergencyContactDTO, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            EmergencyContact createdEmergencyContact = emergencyContactService.createEmergencyContact(userId, emergencyContactDTO);
            if (createdEmergencyContact != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(createdEmergencyContact);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<EmergencyContact> getEmergencyContactById(@PathVariable Long id, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            EmergencyContact emergencyContact = emergencyContactService.getEmergencyContact(id, userId);
            if (emergencyContact != null){
                return ResponseEntity.ok(emergencyContact);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/getEmergencyContactsFromUser")
    public ResponseEntity<List<EmergencyContact>> getAllEmergencyContactsFromUser(HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            List<EmergencyContact> emergencyContacts = emergencyContactService.getAllEmergencyContactsFromUser(userId);
            if (emergencyContacts != null){
                return ResponseEntity.ok(emergencyContacts);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("{id}/updateEmergencyContact")
    public ResponseEntity<EmergencyContact> updateEmergencyContact(@PathVariable Long id, @RequestBody EmergencyContactDTO emergencyContactDTO, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            EmergencyContact emergencyContact = emergencyContactService.updateEmergencyContact(id, emergencyContactDTO, userId);
            if (emergencyContact != null){
                return ResponseEntity.ok(emergencyContact);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> deleteEmergencyContact(@PathVariable Long id, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            emergencyContactService.deleteEmergencyContact(id, userId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
