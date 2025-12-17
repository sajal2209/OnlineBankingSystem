//package com.bankingsystem.dto;
//
//import lombok.Data;
//
//@Data
//public class LoginRequest {
//    private String username;
//    private String password;
//    private String role;
//
//    // Getters and Setters
//}
package com.bankingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;
}
