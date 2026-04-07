package com.example.demo.controller;

import com.example.demo.payloads.ApiResponse;
import com.example.demo.payloads.dtos.UserDto;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    //create User
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody  UserDto dto) {
        UserDto dtoUser = userService.createUser(dto);

        return new ResponseEntity<>(dtoUser, HttpStatus.CREATED);
    }

    //update user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody  UserDto dto,
                                              @PathVariable Long userId) {

        UserDto userDto = this.userService.updateUser(dto, userId);
        return ResponseEntity.ok(userDto);
    }


    //delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        this.userService.deleteUser(userId);

        return new ResponseEntity<>(new ApiResponse("User deleted", true), HttpStatus.OK);
    }


    //GET All users
    public ResponseEntity<List<UserDto>> getAllUsers() {
//        List<UserDto> userDtos = this.userService.getAllUsers();
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    //get single user by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserDto dto = this.userService.getUserById(userId);

        //return new ResponseEntity<>(new ApiResponse("User deleted", true), HttpStatus.OK);
        return ResponseEntity.ok(dto);
    }

}
