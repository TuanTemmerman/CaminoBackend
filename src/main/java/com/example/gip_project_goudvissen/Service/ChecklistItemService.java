package com.example.gip_project_goudvissen.Service;

import com.example.gip_project_goudvissen.DTO.ChecklistItemDTO;
import com.example.gip_project_goudvissen.Entity.Checklist;
import com.example.gip_project_goudvissen.Entity.ChecklistItem;
import com.example.gip_project_goudvissen.Entity.User;
import com.example.gip_project_goudvissen.Repository.ChecklistItemRepository;
import com.example.gip_project_goudvissen.Repository.ChecklistRepository;
import com.example.gip_project_goudvissen.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class ChecklistItemService {

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private UserRepository userRepository;

    public ChecklistItem addChecklistItem(Long userId, Long checklistId, ChecklistItemDTO checklistItemDTO){
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist not found"));
        if (Objects.equals(userId, checklist.getUser().getId())) {
            ChecklistItem checklistItem = new ChecklistItem();
            checklistItem.setChecklist(checklist);
            checklistItem.setItemName(checklistItemDTO.getItemName());
            return checklistItemRepository.save(checklistItem);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to add item to this checklist");
        }
    }

    public List<ChecklistItem> getAllChecklistItemsFromChecklist(Long checklistId, Long userId){
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist not found"));
        if (Objects.equals(checklist.getUser().getId(), userId)){
            return checklistItemRepository.getChecklistItemByChecklist_Id(checklistId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to see this checklist");
        }
    }

    public ChecklistItem updateChecklistItem(Long checklistItemId, Long checklistId, ChecklistItemDTO checklistItemDTO, Long userId){
        ChecklistItem checklistItem = checklistItemRepository.findById(checklistItemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist item not found"));
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist not found"));
        if (Objects.equals(checklist.getUser().getId(), userId)){
            checklistItem.setItemName(checklistItemDTO.getItemName());
            return checklistItemRepository.save(checklistItem);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to edit item of this checklist");
        }
    }

    public void deleteChecklistItem(Long checklistItemId, Long checklistId, Long userId){
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist not found"));
        ChecklistItem checklistItem = checklistItemRepository.findById(checklistItemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist item not found"));
        if (Objects.equals(checklist.getUser().getId(), userId)){
            checklistItemRepository.delete(checklistItem);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to delete item of this checklist");
        }
    }
}
