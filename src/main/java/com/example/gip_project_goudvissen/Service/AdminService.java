package com.example.gip_project_goudvissen.Service;

import com.example.gip_project_goudvissen.Entity.Role;
import com.example.gip_project_goudvissen.Entity.User;
import com.example.gip_project_goudvissen.Repository.RoleRepository;
import com.example.gip_project_goudvissen.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private BlogService blogService;

    @Transactional
    public void createAdminIfNotExists() {
        if (!adminUserExists()) {
            createAdmin();
        }
    }

    private boolean adminUserExists() {
        return userRepository.existsByUsername("admin");
    }

    private void createAdmin() {
        User user = new User();
        user.setFirstname("Ad");
        user.setLastname("Min");
        user.setEmail("admin@example.com");
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("password"));

        Role adminRole = roleRepository.findByName("Admin")
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
        user.setRoles(Collections.singleton(adminRole));

        User admin = userRepository.save(user);
        blogService.createBlog(admin);
    }
}




