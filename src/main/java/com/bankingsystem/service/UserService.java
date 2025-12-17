
package com.bankingsystem.service;

import com.bankingsystem.dto.RegisterCustomerRequest;
import com.bankingsystem.enums.Role;
import com.bankingsystem.entity.User;
import com.bankingsystem.exception.AuthException;
import com.bankingsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public User registerCustomer(RegisterCustomerRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new AuthException("Username already exists");
        }
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new AuthException("Email already exists");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword())); // store BCrypt
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        return userRepo.save(user);
    }

    public User authenticate(String username, String rawPassword) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new AuthException("Invalid credentials"));
        if (!user.isActive()) {
            throw new AuthException("User inactive");
        }
        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new AuthException("Invalid credentials");
        }
        return user;
    }
}
