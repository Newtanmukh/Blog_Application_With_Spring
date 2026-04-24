package com.example.demo.payloads.dtos;

import com.example.demo.entities.Comment;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String title;
    @Column(length = 10000)
    private String content;
    private String imageName;
    private LocalDate addDate;

    //Using dto classes, good thing will be that it wont form a recursion loop.
    //that's why we are using the dto classes instead of original classes(CategoryDto instead of Category etc.)
    private CategoryDto category;
    private UserDto user;
    private Set<CommentDto> comments = new HashSet<>();
}
