package com.japanese.study_app.service.category;

import com.japanese.study_app.dto.CategoryDto;
import com.japanese.study_app.exceptions.AlreadyExistsException;
import com.japanese.study_app.exceptions.CategoryNotFoundException;
import com.japanese.study_app.model.Category;
import com.japanese.study_app.repository.CategoryRepository;
import com.japanese.study_app.request.AddCategoryRequest;
import com.japanese.study_app.service.word.WordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final WordService wordService;

    public CategoryService(CategoryRepository categoryRepository, WordService wordService) {
        this.categoryRepository = categoryRepository;
        this.wordService = wordService;
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found."));
        return convertToCategoryDto(category);
    }

    @Override
    public CategoryDto getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category '" + name + "' not found."));
        return convertToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::convertToCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto addCategory(AddCategoryRequest request) {
//        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
//                .map(categoryRepository :: save)
//                .orElseThrow(() -> new AlreadyExistsException(category.getName() + " already exists."));
        if (categoryRepository.existsByName(request.name())) {
            throw new AlreadyExistsException("Category " + request.name() + " already exists!");
        }

        Category category = categoryRepository.save(new Category(request.name()));
        return convertToCategoryDto(category);
    }

    @Override
    public Category updateCategoryContents(Category category) {
//        return Optional.ofNullable(getCategoryByName(category.getName())).map(categoryToUpdate -> {
//            Collection<Word> words = categoryToUpdate.getWords();
//            words.addAll(category.getWords());
//            categoryToUpdate.setWords(words);
//            return categoryRepository.save(categoryToUpdate);
//        }).orElseThrow(() -> new CategoryNotFoundException("Category " + category.getName() + " not found to update."));
        return new Category();
    }

    @Override
    public Category updateCategoryName(Category category, Long id) {
        return null;
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new CategoryNotFoundException("Category with id " + id + " not found to delete.");
                });
    }

    @Override
    public void deleteCategoryByName(String name) {
        categoryRepository.findByName(name)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new CategoryNotFoundException("Category " + name + " not found to delete.");
                });
    }

    private CategoryDto convertToCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getWords().stream().map(wordService::convertWordToDto).collect(Collectors.toList())
        );
    }
}
