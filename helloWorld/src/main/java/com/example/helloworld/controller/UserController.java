package com.example.helloworld.controller;

import com.example.helloworld.model.AuthRequest;
import com.example.helloworld.model.User;
import com.example.helloworld.service.AuthenticationService;
import com.example.helloworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    // Original registration endpoint - updated with better error handling
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User createdUser = userService.register(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Registration failed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // New authentication endpoints
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequest authRequest) {
        try {
            Map<String, Object> response = authenticationService.authenticate(authRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = Map.of("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of("error", "Login failed");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        try {
            Map<String, String> response = authenticationService.logout();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = Map.of("error", "Logout failed");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            // Extract username from token (you might want to use a filter for this)
            String username = token.replace("Bearer ", "");
            // Note: In a real implementation, you'd extract from JWT properly
            // This is simplified for demonstration
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Fetch all users
    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
