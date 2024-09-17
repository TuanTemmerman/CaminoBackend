package com.example.gip_project_goudvissen.Controller;

import antlr.Token;
import com.example.gip_project_goudvissen.DTO.UserDTO;
import com.example.gip_project_goudvissen.DTO.UserUpdateDTO;
import com.example.gip_project_goudvissen.Entity.Coordinate;
import com.example.gip_project_goudvissen.Entity.User;
import com.example.gip_project_goudvissen.Service.JwtTokenService;
import com.example.gip_project_goudvissen.Service.TokenExtractor;
import com.example.gip_project_goudvissen.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        User registeredUser = userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            User user = userService.findUserById(userId);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            List<User> users = userService.getAllUsers(userId);
            if (users != null) {
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("{userId}/getLocation")
    public ResponseEntity<?> getUserLocation(@PathVariable Long userId){
        Coordinate coordinate = userService.getUserLocation(userId);
        if (coordinate != null){
            return ResponseEntity.ok(coordinate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not have a location");
        }
    }

    @PutMapping("/updateLocation")
    public ResponseEntity<User> updateLocation(@RequestBody Coordinate newLocation, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            User updatedUser = userService.updateLocation(userId, newLocation);
            if (updatedUser != null){
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateDTO userUpdateDto, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            User updatedUser = userService.updateUserName(userId, userUpdateDto);
            if (updatedUser != null){
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/updateDistance")
    public ResponseEntity<User> updateDistance(@RequestParam int distance, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            User updatedUser = userService.updateUserDistance(userId, distance);
            if (updatedUser != null){
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<User> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            User updatedUser = userService.changePassword(userId, oldPassword, newPassword);
            if (updatedUser != null){
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/addToRoute/{routeId}")
    public ResponseEntity<Void> addUserToRoute(@PathVariable Long routeId, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            userService.addUserToRoute(userId, routeId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/RemoveFromRoute")
    public ResponseEntity<Void> removeUserFromRoute(HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            userService.removeUserFromRoute(userId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
