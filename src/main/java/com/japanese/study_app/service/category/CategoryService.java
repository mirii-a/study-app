package com.japanese.study_app.service.category;

import com.japanese.study_app.dto.CategoryDto;
import com.japanese.study_app.exceptions.AlreadyExistsException;
import com.japanese.study_app.exceptions.CategoryNotFoundException;
import com.japanese.study_app.model.Category;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.CategoryRepository;
import com.japanese.study_app.request.AddCategoryRequest;
import com.japanese.study_app.service.word.WordService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public CategoryDto getCategoryDtoById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found."));
        return convertToCategoryDto(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found."));
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
        return Optional.ofNullable(request.name())
                .filter(name -> !categoryRepository.existsByName(name))
                .map(name -> categoryRepository.save(new Category(name)))
                .map(this::convertToCategoryDto)
                .orElseThrow(() -> new AlreadyExistsException("Category " + request.name() + " already exists!"));
    }

    private void dealWithCategoriesInCategoryRepository(Set<Category> categoryInput) {
        categoryInput.forEach(category -> {
            boolean categoryExists = categoryRepository.existsByName(category.getName());
            if (!categoryExists) {
                Category newCategory = new Category(category.getName());
                categoryRepository.save(newCategory);
                category.setId(newCategory.getId());
                category.setName(newCategory.getName());
            } else {
                Optional<Category> categoryFromDb = categoryRepository.findByName(category.getName());
                categoryFromDb.ifPresent(value -> {
                    category.setId(value.getId());
                    category.setName(value.getName());
                });
            }
        });
    }

    public void addWordToCategoryMapping(Word word) {
        List<Category> newCategories = word.getCategory().stream()
                .map(category -> getCategoryById(category.getId()))
                .toList();

        newCategories.forEach(category -> {
            Collection<Word> words = category.getWords();
            if (!words.contains(word)) {
                words.add(word);
                category.setWords(words);
                categoryRepository.save(category);
            }
        });
    }

//    @Override
//    public Category updateCategoryContents(Category category) {
////        return Optional.ofNullable(getCategoryByName(category.getName())).map(categoryToUpdate -> {
////            Collection<Word> words = categoryToUpdate.getWords();
////            words.addAll(category.getWords());
////            categoryToUpdate.setWords(words);
////            return categoryRepository.save(categoryToUpdate);
////        }).orElseThrow(() -> new CategoryNotFoundException("Category " + category.getName() + " not found to update."));
//        return new Category();
//    }

//    @Override
//    public Category updateCategoryName(Category category, Long id) {
//        return null;
//    }

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

    @Override
    public boolean checkIfCategoryExistsByName(String category) {
        return categoryRepository.existsByName(category);
    }

    public Word updateCategories(Word wordToUpdate, Set<Category> updatedCategories) {
        checkIfCategoriesHaveBeenRemoved(wordToUpdate, updatedCategories);
        dealWithCategories(updatedCategories);
        wordToUpdate.setCategory(updatedCategories);
        // Ensure that the new Category (if any) has the new word set
        addWordToCategoryMapping(wordToUpdate);
        return wordToUpdate;
    }

    public Set<Category> dealWithCategories(Set<Category> categories) {
        dealWithCategoriesInCategoryRepository(categories);
        return categories;
    }

    public void removeWordFromCategory(Category category, Word word) {
        removeWordFromCategoryMapping(category, word);
    }

    private CategoryDto convertToCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getWords().stream().map(wordService::convertWordToDto).collect(Collectors.toList())
        );
    }

    private void checkIfCategoriesHaveBeenRemoved(Word word, Set<Category> updatedCategoryList) {
        for (Category category : word.getCategory()) {
            if (!updatedCategoryList.contains(category)) {
                // Remove mapping from database
                removeWordFromCategory(category, word);
            }
        }
    }

    private void removeWordFromCategoryMapping(Category category, Word word) {
        Collection<Word> wordsForCategory = category.getWords();
        wordsForCategory.removeIf(wordInCategory -> wordInCategory.getJapaneseWord().equals(word.getJapaneseWord()));
        category.setWords(wordsForCategory);
        categoryRepository.save(category);
    }
}
