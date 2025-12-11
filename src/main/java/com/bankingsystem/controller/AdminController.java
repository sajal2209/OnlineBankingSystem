package com.bankingsystem.controller;

import com.bankingsystem.entity.User;
import com.bankingsystem.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;
    public AdminController(UserRepository userRepository) { this.userRepository = userRepository; }

    @GetMapping("/users")
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User deleted";
    }
}
