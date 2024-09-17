package com.example.gip_project_goudvissen.Service;

import com.example.gip_project_goudvissen.DTO.UserDTO;
import com.example.gip_project_goudvissen.DTO.UserUpdateDTO;
import com.example.gip_project_goudvissen.Entity.*;
import com.example.gip_project_goudvissen.Repository.RoleRepository;
import com.example.gip_project_goudvissen.Repository.RouteRepository;
import com.example.gip_project_goudvissen.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private BlogService blogService;

    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setPhoneNumber(userDTO.getPhonenumber());
        user.setTotalDistance(0);
        Role userRole = roleRepository.findByName("User")
                .orElseThrow(() -> new RuntimeException("User role not found"));
        user.setRoles(Collections.singleton(userRole));
        User registeredUser = userRepository.save(user);
        blogService.createBlog(registeredUser);
        return registeredUser;
    }

    public User findUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<User> getAllUsers(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (Role.ROLE_ADMIN.equals(role.getName())) {
                return userRepository.findAll();
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to view this");
            }
        }
        return List.of(user);
    }

    public Coordinate getUserLocation(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getLocation() != null) {
            Coordinate coordinate = new Coordinate();
            coordinate.setX(user.getLocation().getX());
            coordinate.setY(user.getLocation().getY());
            return coordinate;
        } else {
            return null;
        }
    }

    public User updateLocation(Long userId, Coordinate location) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setLocation(location);
        return userRepository.save(user);
    }

    public User updateUserName(Long userId, UserUpdateDTO userUpdateDTO){
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        foundUser.setFirstname(userUpdateDTO.getFirstname());
        foundUser.setLastname(userUpdateDTO.getLastname());
        return userRepository.save(foundUser);
    }

    public User updateUserDistance(Long userId, int distance){
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        int currentDistance = foundUser.getTotalDistance();
        currentDistance += distance;
        foundUser.setTotalDistance(currentDistance);
        return foundUser;
    }

    public User changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        // Valideer out wachtwoord
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public void deleteUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (Role.ROLE_USER.equals(role.getName())) {
                user.getRoles().clear();
                userRepository.deleteById(userId);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to view this");
            }
        }

    }

    public void addUserToRoute(Long userId, Long routeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found"));
        user.setRoute(route);
        userRepository.save(user);
    }
    public void removeUserFromRoute(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setRoute(null);
        userRepository.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
