package com.example.demo.services;

import com.example.demo.payloads.dtos.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long catId ,CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto getCategory(Long categoryId);

    List<CategoryDto>  getAllCategories();
}
