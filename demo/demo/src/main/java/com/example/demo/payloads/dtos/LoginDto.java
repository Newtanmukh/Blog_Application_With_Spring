package com.example.demo.payloads.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {

    @Schema(description = "User email address", example = "user@example.com")
    @NotEmpty(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @Schema(description = "User password", example = "password123")
    @NotEmpty(message = "Password is required")
    private String password;
}
