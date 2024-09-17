package com.example.gip_project_goudvissen.Service;

import com.example.gip_project_goudvissen.DTO.EmergencyContactDTO;
import com.example.gip_project_goudvissen.Entity.EmergencyContact;
import com.example.gip_project_goudvissen.Entity.User;
import com.example.gip_project_goudvissen.Repository.EmergencyContactRepository;
import com.example.gip_project_goudvissen.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class EmergencyContactService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    public EmergencyContact addExistingUserAsEmergencyContact(Long userId, Long contactUserId, String customName){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User contactUser = userRepository.findById(contactUserId).orElseThrow(() -> new RuntimeException("Contact user not found"));
        if (Objects.equals(user, contactUser)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User cannot add himself as contact user");
        }
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setUser(user);
        emergencyContact.setCustomName(customName);
        emergencyContact.setPhoneNumber(contactUser.getPhoneNumber());
        emergencyContact.setEmail(contactUser.getEmail());
        return emergencyContactRepository.save(emergencyContact);
    }

    public EmergencyContact createEmergencyContact(Long userId, EmergencyContactDTO emergencyContactDTO){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setCustomName(emergencyContactDTO.getCustomName());
        emergencyContact.setPhoneNumber(emergencyContactDTO.getPhoneNumber());
        emergencyContact.setEmail(emergencyContactDTO.getEmail());
        emergencyContact.setUser(user);
        return emergencyContactRepository.save(emergencyContact);
    }

    public EmergencyContact getEmergencyContact(Long id, Long userId){
        EmergencyContact emergencyContact = emergencyContactRepository.findById(id).orElseThrow(() -> new RuntimeException("Emergency Contact not found"));
        if (Objects.equals(emergencyContact.getUser().getId(), userId)){
            return emergencyContact;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to see this emergency contact");
        }
    }

    public List<EmergencyContact> getAllEmergencyContactsFromUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return emergencyContactRepository.findEmergencyContactsByUser(user);
    }

    public EmergencyContact updateEmergencyContact(Long id, EmergencyContactDTO emergencyContactDTO, Long userId){
        EmergencyContact emergencyContact = emergencyContactRepository.findById(id).orElseThrow(() -> new RuntimeException("Emergency Contact not found"));
        if (Objects.equals(emergencyContact.getUser().getId(), userId)){
            emergencyContact.setCustomName(emergencyContactDTO.getCustomName());
            emergencyContact.setPhoneNumber(emergencyContactDTO.getPhoneNumber());
            emergencyContact.setEmail(emergencyContactDTO.getEmail());
            return emergencyContactRepository.save(emergencyContact);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to update this emergency contact");
        }
    }

    public void deleteEmergencyContact(Long id, Long userId){
        EmergencyContact emergencyContact = emergencyContactRepository.findById(id).orElseThrow(() -> new RuntimeException("Emergency Contact not found"));
        if (Objects.equals(emergencyContact.getUser().getId(), userId)){
            emergencyContactRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to delete this emergency contact");
        }
    }
}
