package com.example.demo.payloads.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    @NotBlank
    @Size(min = 4, max = 200, message = "Min size of category title is 4")
    private String categoryTitle;

    @NotBlank
    @Size(min = 4, max = 200, message = "Min size of category Description is 4")
    private String categoryDescription;
}
