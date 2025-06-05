package com.japanese.study_app.service.category;

import com.japanese.study_app.dto.CategoryDto;
import com.japanese.study_app.model.Category;
import com.japanese.study_app.request.AddCategoryRequest;

import java.util.List;

public interface ICategoryService {

    // defines business logic layer, abstracts operations that can be 
    // performed on Category entity, often using CategoryRepository to perform
    // database operations.
    // Encapsulates business rules and logic, acts as an intermediary between
    // the controller and repository layers.
    // Provides an abstraction over the repository, making it easier to change
    // the underlying data access implementation if needed.

    Category getCategoryById(Long id);

    CategoryDto getCategoryDtoById(Long id);

    CategoryDto getCategoryByName(String name);

    List<CategoryDto> getAllCategories();

    CategoryDto addCategory(AddCategoryRequest category);

    //    Category updateCategoryContents(Category category);
    void deleteCategoryById(Long id);

    void deleteCategoryByName(String name);

    boolean checkIfCategoryExistsByName(String category);
}
