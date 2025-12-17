package com.bankingsystem.controller;

import com.bankingsystem.dto.CreateBankerRequest;
import com.bankingsystem.entity.User;
import com.bankingsystem.repository.UserRepository;
import com.bankingsystem.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;

    private final AdminService adminService;

    public AdminController(UserRepository userRepository, AdminService adminService) {
        this.userRepository = userRepository;
        this.adminService = adminService;
    }
    @GetMapping("/users")
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User deleted";
    }
    @PostMapping("/create-banker")
    public ResponseEntity<String> createBanker(@Valid @RequestBody CreateBankerRequest request) {
        adminService.createBanker(request);
        return ResponseEntity.ok("Banker Created Successfully.");
    }
}
