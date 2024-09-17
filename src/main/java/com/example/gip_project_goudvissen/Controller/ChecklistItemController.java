package com.example.gip_project_goudvissen.Controller;

import com.example.gip_project_goudvissen.DTO.ChecklistItemDTO;
import com.example.gip_project_goudvissen.Entity.ChecklistItem;
import com.example.gip_project_goudvissen.Service.ChecklistItemService;
import com.example.gip_project_goudvissen.Service.JwtTokenService;
import com.example.gip_project_goudvissen.Service.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/ChecklistItem")
public class ChecklistItemController {

    @Autowired
    private ChecklistItemService checklistItemService;

    @Autowired
    private JwtTokenService tokenService;

    @PostMapping("/addChecklistItem/{checklistId}")
    public ResponseEntity<ChecklistItem> addChecklistItem(@PathVariable Long checklistId, @RequestBody ChecklistItemDTO checklistItemDTO, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            ChecklistItem createdChecklistItem = checklistItemService.addChecklistItem(userId, checklistId, checklistItemDTO);
            if (createdChecklistItem != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdChecklistItem);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{checklistId}/getAllChecklistItems")
    public ResponseEntity<List<ChecklistItem>> getAllChecklistItems(@PathVariable Long checklistId, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            List<ChecklistItem> checklistItems = checklistItemService.getAllChecklistItemsFromChecklist(checklistId, userId);
            if (checklistItems != null){
                return ResponseEntity.ok(checklistItems);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PutMapping("/{checklistId}/updateChecklistItem/{checklistItemId}")
    public ResponseEntity<ChecklistItem> updateChecklistItem(@PathVariable Long checklistId, @PathVariable Long checklistItemId, @RequestBody ChecklistItemDTO checklistItemDTO, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            ChecklistItem updatedChecklistItem = checklistItemService.updateChecklistItem(checklistItemId, checklistId, checklistItemDTO, userId);
            if (updatedChecklistItem != null){
                return ResponseEntity.ok(updatedChecklistItem);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{checklistId}/deleteChecklistItem/{checklistItemId}")
    public ResponseEntity<Void> deleteChecklistItem(@PathVariable Long checklistId, @PathVariable Long checklistItemId, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            checklistItemService.deleteChecklistItem(checklistItemId, checklistId, userId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
