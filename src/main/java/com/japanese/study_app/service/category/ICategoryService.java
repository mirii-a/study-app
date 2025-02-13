package com.japanese.study_app.service.category;

import java.util.List;

import com.japanese.study_app.dto.CategoryDto;
import com.japanese.study_app.model.Category;
import com.japanese.study_app.request.AddCategoryRequest;

public interface ICategoryService {

    // defines business logic layer, abstracts operations that can be 
    // performed on Category entity, often using CategoryRepository to perform
    // database operations.
    // ENcapsulates business rules and logic, acts as an intermediary between
    // the controller and repository layers.
    // Provides an abstraction over the repository, making it easier to change
    // the underlying data access implementation if needed.

    CategoryDto getCategoryById(Long id);
    CategoryDto getCategoryByName(String name);
    List<CategoryDto> getAllCategories();
    
    CategoryDto addCategory(AddCategoryRequest category);
    Category updateCategoryContents(Category category);
    Category updateCategoryName(Category category, Long id);
    void deleteCategoryById(Long id);
    void deleteCategoryByName(String name);
}
