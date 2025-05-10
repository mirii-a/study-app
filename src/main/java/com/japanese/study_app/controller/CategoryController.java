package com.japanese.study_app.controller;

import com.japanese.study_app.dto.CategoryDto;
import com.japanese.study_app.exceptions.AlreadyExistsException;
import com.japanese.study_app.exceptions.CategoryNotFoundException;
import com.japanese.study_app.request.AddCategoryRequest;
import com.japanese.study_app.response.ApiResponse;
import com.japanese.study_app.service.category.ICategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<CategoryDto> allCategories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("All categories retrieved successfully.", allCategories));
        } catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to find categories.", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/get/category/name/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            CategoryDto category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Category retrieved successfully.", category));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/category/id/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            CategoryDto category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category retrieved successfully.", category));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody AddCategoryRequest category){
        try {
            CategoryDto newCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Category added successfully.", newCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id){
        try{
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category with id " + id + " successfully deleted.", null));
        } catch (CategoryNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<ApiResponse> deleteCategoryByName(@PathVariable String name){
        try{
            categoryService.deleteCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Category " + name + " successfully deleted.", null));
        } catch (CategoryNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
