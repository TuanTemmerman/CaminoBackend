package com.example.gip_project_goudvissen.Repository;

import com.example.gip_project_goudvissen.Entity.EmergencyContact;
import com.example.gip_project_goudvissen.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
    List<EmergencyContact> findEmergencyContactsByUser(User user);
}
