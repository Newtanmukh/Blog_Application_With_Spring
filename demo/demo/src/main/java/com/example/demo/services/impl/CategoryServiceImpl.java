package com.example.demo.services.impl;

import com.example.demo.entities.Category;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payloads.dtos.CategoryDto;
import com.example.demo.repositories.CategoryRepo;
import com.example.demo.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        category = categoryRepo.save(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = categoryRepo.findById(categoryDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", categoryDto.getId()));

        category.setCategoryDescription(categoryDto.getCategoryDescription());
        category.setCategoryTitle(categoryDto.getCategoryTitle());

        category  = categoryRepo.save(category);

        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public void deleteCategory(CategoryDto categoryDto) {
        Category category = categoryRepo.findById(categoryDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", categoryDto.getId()));
        categoryRepo.delete(category);
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", categoryId));

        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepo.findAll().stream().map(category -> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
    }
}
