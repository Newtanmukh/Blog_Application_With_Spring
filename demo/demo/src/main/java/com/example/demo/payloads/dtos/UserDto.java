package com.example.demo.payloads.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    @Schema(description = "User ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "User full name", example = "John Doe")
    @NotEmpty
    private String name;

    @Schema(description = "User email address", example = "john@example.com")
    @Email(message = "given email address isn't valid")
    @Size(min = 4, message = "minimum of 4 characters")
    private String email;

    @Schema(description = "User password", example = "password123")
    @NotEmpty
    @Size(min =  3, max = 30, message = "Password must be between 3 and 30 characters.")
    private String password;

    @Schema(description = "Short bio about the user", example = "Java developer")
    @NotNull
    @NotEmpty
    private String about;

}
