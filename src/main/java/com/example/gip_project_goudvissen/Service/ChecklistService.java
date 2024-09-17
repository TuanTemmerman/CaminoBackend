package com.example.gip_project_goudvissen.Service;

import com.example.gip_project_goudvissen.DTO.ChecklistDTO;
import com.example.gip_project_goudvissen.Entity.Checklist;
import com.example.gip_project_goudvissen.Entity.User;
import com.example.gip_project_goudvissen.Repository.ChecklistRepository;
import com.example.gip_project_goudvissen.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private UserRepository userRepository;

    public Checklist addChecklist(Long userId, ChecklistDTO checklistDTO){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Checklist checklist = new Checklist();
        checklist.setTitle(checklistDTO.getTitle());
        checklist.setUser(user);
        return checklistRepository.save(checklist);
    }

    public Checklist getChecklist(Long userId, Long checklistId){
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist not found"));
        if (Objects.equals(userId, checklist.getUser().getId())){
            return checklist;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to view this checklist");
        }
    }

    public Checklist updateChecklist(Long userId, Long checklistId, ChecklistDTO checklistDTO){
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist not found"));
        if (Objects.equals(userId, checklist.getUser().getId())){
            checklist.setTitle(checklistDTO.getTitle());
            return checklistRepository.save(checklist);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to view this checklist");
        }
    }

    public void deleteChecklist(Long userId, Long checklistId){
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist not found"));
        if (Objects.equals(userId, checklist.getUser().getId())){
            checklistRepository.delete(checklist);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to delete this checklist");
        }
    }

}
