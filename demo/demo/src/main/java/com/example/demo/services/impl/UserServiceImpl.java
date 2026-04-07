package com.example.demo.services.impl;

import com.example.demo.entities.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payloads.dtos.UserDto;
import com.example.demo.repositories.UserRepo;
import com.example.demo.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto dto) {
        User user = dtoToUser(dto);
        User savedUser = this.userRepo.save(user);

        return userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto user, Long userId) {
        User dbUser  = userRepo.findById(Long.valueOf(userId)).orElseThrow((() -> new ResourceNotFoundException("User", "id", userId)));

        UserDto dto = userToDto(dbUser);
        User savedUser = dtoToUser(dto);
        savedUser =  this.userRepo.save(savedUser);

        return userToDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User dbUser  = userRepo.findById(Long.valueOf(userId)).get();
        return userToDto(dbUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepo.findAll();

        return users.stream().map(this::userToDto).toList();
    }

    @Override
    public void deleteUser(Long userId) {
        User dbUser  = userRepo.findById(Long.valueOf(userId)).get();
        userRepo.delete(dbUser);
    }


    private User dtoToUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
//        User user = new User();
//        user.setAbout(userDto.getAbout());
//        user.setId(userDto.getId());
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setPassword(userDto.getPassword());

        return user;
    }

    private UserDto userToDto(User userDto) {
        UserDto dto = this.modelMapper.map(userDto, UserDto.class);

//        dto.setId(userDto.getId());
//        dto.setName(userDto.getName());
//        dto.setEmail(userDto.getEmail());
//        dto.setPassword(userDto.getPassword());

        return dto;
    }
}
