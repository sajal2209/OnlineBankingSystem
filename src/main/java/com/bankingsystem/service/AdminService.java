
package com.bankingsystem.service;

import com.bankingsystem.dto.CreateBankerRequest;
import com.bankingsystem.entity.User;
import com.bankingsystem.enums.Role;
import com.bankingsystem.exception.AuthException;
import com.bankingsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public AdminService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public User createBanker(CreateBankerRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new AuthException("Username already exists");
        }
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new AuthException("Email already exists");
        }
        User banker = new User();
        banker.setUsername(req.getUsername());
        banker.setEmail(req.getEmail());
        banker.setPassword(encoder.encode(req.getPassword())); // BCrypt
        banker.setRole(Role.BANKER);
        banker.setActive(true);
        return userRepo.save(banker);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        userRepo.deleteById(id);
    }

}
