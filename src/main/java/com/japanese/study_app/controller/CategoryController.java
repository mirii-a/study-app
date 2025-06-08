package com.japanese.study_app.controller;

import com.japanese.study_app.dto.CategoryDto;
import com.japanese.study_app.request.AddCategoryRequest;
import com.japanese.study_app.response.ApiResponse;
import com.japanese.study_app.service.category.ICategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        List<CategoryDto> allCategories = categoryService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse("All categories retrieved successfully.", allCategories));
    }

    @GetMapping("/get/category/name/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        CategoryDto category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(new ApiResponse("Category retrieved successfully.", category));
    }

    @GetMapping("/get/category/id/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryDtoById(id);
        return ResponseEntity.ok(new ApiResponse("Category retrieved successfully.", category));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody AddCategoryRequest category) {
        CategoryDto newCategory = categoryService.addCategory(category);
        return ResponseEntity.ok(new ApiResponse("Category added successfully.", newCategory));
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(new ApiResponse("Category with id " + id + " successfully deleted.", null));
    }

    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<ApiResponse> deleteCategoryByName(@PathVariable String name) {
        categoryService.deleteCategoryByName(name);
        return ResponseEntity.ok(new ApiResponse("Category " + name + " successfully deleted.", null));
    }
}
