package com.example.gip_project_goudvissen.Repository;

import com.example.gip_project_goudvissen.Entity.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    List<ChecklistItem> getChecklistItemByChecklist_Id(Long checklistId);
}
