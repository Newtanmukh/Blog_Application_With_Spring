package com.example.demo.services;

import com.example.demo.payloads.dtos.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto Userto);

    UserDto updateUser(UserDto user, Long userId);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUser(Long userId);
}
