package com.example.demo.payloads.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty
    private String name;

    @Email(message = "given email address isn't valid")
    @Size(min = 4, message = "minimum of 4 characters")
    private String email;


    @NotEmpty
    @Size(min =  3, max = 30, message = "Password must be between 3 and 30 characters.")
    private String password;

    @NotNull
    @NotEmpty
    private String about;

}
