
package com.bankingsystem.util;

import com.bankingsystem.dto.CreateBankerRequest;
import com.bankingsystem.dto.RegisterCustomerRequest;
import com.bankingsystem.entity.User;
import com.bankingsystem.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static User toCustomer(RegisterCustomerRequest request, PasswordEncoder encoder) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        return user;
    }

    public static User toBanker(CreateBankerRequest request, PasswordEncoder encoder) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.BANKER);
        user.setActive(true);
        return user;
    }
}

