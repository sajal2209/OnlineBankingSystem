
package com.bankingsystem.controller;

import com.bankingsystem.dto.LoginRequest;
import com.bankingsystem.dto.RefreshTokenRequest;
import com.bankingsystem.dto.RegisterCustomerRequest;
import com.bankingsystem.dto.TokenResponse;
import com.bankingsystem.entity.User;
import com.bankingsystem.repository.UserRepository;
import com.bankingsystem.security.JwtUtil;
import com.bankingsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/register-customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody RegisterCustomerRequest request) {
        userService.registerCustomer(request);
        return ResponseEntity.ok("User registered successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticate(request.getUsername(), request.getPassword());
        String access = jwtUtil.generateAccessToken(
                user.getUsername(), user.getRole().name(), user.getId(), user.isActive()
        );
        String refresh = jwtUtil.generateRefreshToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new TokenResponse(access, refresh));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest req) {
        if (!jwtUtil.isValid(req.getRefreshToken())) {
            return ResponseEntity.status(401).build();
        }
        var claims = jwtUtil.parseClaims(req.getRefreshToken());
        String username = claims.getSubject();
        String role = claims.get("role", String.class);
        String access = jwtUtil.generateAccessToken(username, role, null, true);
        return ResponseEntity.ok(new TokenResponse(access, req.getRefreshToken()));
    }

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(request.getRole());
//        userRepository.save(user);
//        return ResponseEntity.ok("User registered successfully");
//    }

//    @PostMapping("/login")
//    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
//        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
//
//        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
//    }


//    @PostMapping("/login")
//    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        if (!user.getRole().equalsIgnoreCase(request.getRole())) {
//            throw new RuntimeException("Role mismatch");
//        }
//
//        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole());
//        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
//
//        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
//    }



//    @PostMapping("/login")
//    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        if (!user.getRole().equalsIgnoreCase(request.getRole())) {
//            throw new RuntimeException("Role mismatch");
//        }
//
//        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole(), user.getId(), user.isActive());
//        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
//
//        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
//    }
//
//    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponse> refresh(@RequestBody Map<String, String> request) {
//        String refreshToken = request.get("refreshToken");
//        String username = jwtUtil.extractUsername(refreshToken);
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String newAccessToken = jwtUtil.generateAccessToken(username, user.getRole(), user.getId(), user.isActive());
//        return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
//    }








//    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponse> refresh(@RequestBody Map<String, String> request) {
//        String refreshToken = request.get("refreshToken");
//        String username = jwtUtil.extractUsername(refreshToken);
//
//        // ✅ Fetch user to get role
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String newAccessToken = jwtUtil.generateAccessToken(username, user.getRole());
//
//        return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
//    }


//    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponse> refresh(@RequestBody Map<String, String> request) {
//        String refreshToken = request.get("refreshToken");
//        String username = jwtUtil.extractUsername(refreshToken);
//        String newAccessToken = jwtUtil.generateAccessToken(username);
//
//        return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
//    }

}
