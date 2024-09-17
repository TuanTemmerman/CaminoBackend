package com.example.gip_project_goudvissen.Repository;

import com.example.gip_project_goudvissen.Entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findByUserId (Long userId);
}
