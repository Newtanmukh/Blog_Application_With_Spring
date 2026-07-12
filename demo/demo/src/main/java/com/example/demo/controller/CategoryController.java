package com.example.demo.controller;

import com.example.demo.payloads.dtos.CategoryDto;
import com.example.demo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@PreAuthorize("hasRole('USER')")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //create
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(this.categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("catId") Long categoryId, @RequestBody CategoryDto categoryDto){
        CategoryDto category = categoryService.updateCategory(categoryId, categoryDto);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId){
        categoryService.deleteCategory(catId);
    }

    //get
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("catId") Long catId){
        return ResponseEntity.ok(categoryService.getCategory(catId));
    }

    //get all
    @GetMapping("/getAllCategories")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
