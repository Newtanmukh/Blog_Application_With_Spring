package com.example.demo.controller;

import com.example.demo.payloads.dtos.CategoryDto;
import com.example.demo.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Category management APIs")
@PreAuthorize("hasRole('USER')")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //create
    @Operation(summary = "Create category", description = "Create a new category")
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(this.categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    //update
    @Operation(summary = "Update category", description = "Update an existing category")
    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("catId") Long categoryId, @RequestBody CategoryDto categoryDto){
        CategoryDto category = categoryService.updateCategory(categoryId, categoryDto);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    //delete
    @Operation(summary = "Delete category", description = "Delete a category by ID")
    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId){
        categoryService.deleteCategory(catId);
    }

    //get
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category")
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("catId") Long catId){
        return ResponseEntity.ok(categoryService.getCategory(catId));
    }

    //get all
    @Operation(summary = "Get all categories", description = "Retrieve all categories")
    @GetMapping("/getAllCategories")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
