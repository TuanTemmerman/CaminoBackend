package com.example.gip_project_goudvissen.Controller;

import com.example.gip_project_goudvissen.DTO.NotesDTO;
import com.example.gip_project_goudvissen.Entity.Notes;
import com.example.gip_project_goudvissen.Service.JwtTokenService;
import com.example.gip_project_goudvissen.Service.NotesService;
import com.example.gip_project_goudvissen.Service.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/Notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @Autowired
    private JwtTokenService tokenService;

    @PostMapping("/addNotes")
    public ResponseEntity<Notes> addNotes(@RequestBody NotesDTO notesDTO, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Notes createdNotes = notesService.addNotes(notesDTO, userId);
            if (createdNotes != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdNotes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{notesId}")
    public ResponseEntity<Notes> getNotes(@PathVariable Long notesId, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Notes notes = notesService.findNotesById(notesId, userId);
            if (notes != null){
                return ResponseEntity.ok(notes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/notesByUser")
    public ResponseEntity<List<Notes>> getNotesFromUser(HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            List<Notes> notes = notesService.getAllNotesFromUser(userId);
            if (notes != null){
                return ResponseEntity.ok(notes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/updateNotes/{notesId}")
    public ResponseEntity<Notes> updateNotes(@RequestBody NotesDTO notesDTO, @PathVariable Long notesId, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Notes notes = notesService.updateNotes(userId, notesDTO, notesId);
            if (notes != null){
                return ResponseEntity.ok(notes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/deleteNotes/{notesId}")
    public ResponseEntity<Notes> deleteNotes(@PathVariable Long notesId, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            notesService.deleteNotes(userId, notesId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
