
package com.bankingsystem.bootstrap;

import com.bankingsystem.entity.User;
import com.bankingsystem.enums.Role;
import com.bankingsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public AdminSeeder(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        userRepo.findByUsername("admin").ifPresentOrElse(
                a -> {}, // exists
                () -> {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setEmail("admin@obs.local");
                    admin.setPassword(encoder.encode("admin@1234")); // BCrypt stored
                    admin.setRole(Role.ADMIN);
                    admin.setActive(true);
                    userRepo.save(admin);
                }
        );
    }
}
