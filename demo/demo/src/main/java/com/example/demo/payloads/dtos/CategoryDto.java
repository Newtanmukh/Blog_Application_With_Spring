package com.example.demo.payloads.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String categoryTitle;
    private String categoryDescription;
}
