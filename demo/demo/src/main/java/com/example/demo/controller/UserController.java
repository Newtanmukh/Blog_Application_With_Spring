package com.example.demo.controller;

import com.example.demo.payloads.ApiResponse;
import com.example.demo.payloads.dtos.UserDto;
import com.example.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management APIs")
@PreAuthorize("@securityAuthorizationService.canAccessAdminUsers(authentication)")
public class UserController {

    @Autowired
    private UserService userService;


    //create User
    @Operation(summary = "Create user", description = "Create a new user")
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody  UserDto dto) {
        UserDto dtoUser = userService.createUser(dto);

        return new ResponseEntity<>(dtoUser, HttpStatus.CREATED);
    }

    //update user
    @Operation(summary = "Update user", description = "Update an existing user by ID")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody  UserDto dto,
                                              @PathVariable Long userId) {

        UserDto userDto = this.userService.updateUser(dto, userId);
        return ResponseEntity.ok(userDto);
    }


    //delete user
    @Operation(summary = "Delete user", description = "Delete a user by ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        this.userService.deleteUser(userId);

        return new ResponseEntity<>(new ApiResponse("User deleted", true), HttpStatus.OK);
    }


    //GET All users
    @Operation(summary = "Get all users", description = "Retrieve all users")
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers() {
//        List<UserDto> userDtos = this.userService.getAllUsers();
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    //get single user by id
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by ID")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserDto dto = this.userService.getUserById(userId);

        //return new ResponseEntity<>(new ApiResponse("User deleted", true), HttpStatus.OK);
        return ResponseEntity.ok(dto);
    }

}
