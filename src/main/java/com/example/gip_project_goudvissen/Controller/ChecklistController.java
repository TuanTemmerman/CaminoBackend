package com.example.gip_project_goudvissen.Controller;

import com.example.gip_project_goudvissen.DTO.ChecklistDTO;
import com.example.gip_project_goudvissen.Entity.Checklist;
import com.example.gip_project_goudvissen.Service.ChecklistService;
import com.example.gip_project_goudvissen.Service.JwtTokenService;
import com.example.gip_project_goudvissen.Service.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Checklist")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

    @Autowired
    private JwtTokenService tokenService;

    @PostMapping("/addChecklist")
    public ResponseEntity<Checklist> addChecklist(@RequestBody ChecklistDTO checklistDTO, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Checklist createdChecklist = checklistService.addChecklist(userId, checklistDTO);
            if (createdChecklist != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdChecklist);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/getChecklist/{checklistId}")
    public ResponseEntity<Checklist> getChecklist(@PathVariable Long checklistId, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Checklist checklist = checklistService.getChecklist(userId, checklistId);
            if (checklist != null) {
                return ResponseEntity.ok(checklist);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/updateChecklist/{checklistId}")
    public ResponseEntity<Checklist> updateChecklist(@PathVariable Long checklistId, @RequestBody ChecklistDTO checklistDTO, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Checklist updatedChecklist = checklistService.updateChecklist(userId, checklistId, checklistDTO);
            if (updatedChecklist != null){
                return ResponseEntity.ok(updatedChecklist);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/deleteChecklist/{checklistId}")
    public ResponseEntity<Void> deleteChecklist(@PathVariable Long checklistId, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            checklistService.deleteChecklist(userId, checklistId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
