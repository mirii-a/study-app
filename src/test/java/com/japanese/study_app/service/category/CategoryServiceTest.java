package com.japanese.study_app.service.category;

import com.japanese.study_app.dto.CategoryDto;
import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.exceptions.CategoryNotFoundException;
import com.japanese.study_app.model.Category;
import com.japanese.study_app.model.EnglishWord;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.CategoryRepository;
import com.japanese.study_app.service.word.WordDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private WordDtoService wordDtoService;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        this.categoryService = new CategoryService(categoryRepository, wordDtoService);
    }

    @Test
    @DisplayName("Should successfully return a CategoryDto by ID")
    void getCategoryDtoById() throws Exception {
        Category testCategory = getCategory();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        when(wordDtoService.convertWordToDto(testCategory.getWords().stream().filter(word -> word.getJapaneseWord().equals("依頼(する)")).findFirst().orElseThrow(() -> new Exception("Test failing due to missing parameters.")))).thenReturn(iraiSuruWordDto());
        when(wordDtoService.convertWordToDto(testCategory.getWords().stream().filter(word -> word.getJapaneseWord().equals("隠れる")).findFirst().orElseThrow(() -> new Exception("Test failing due to missing parameters.")))).thenReturn(kakuReRuWordDto());

        CategoryDto result = categoryService.getCategoryDtoById(1L);

        assertNotNull(result);

        assertEquals(1L, result.id());
        assertEquals("verb", result.name());
        assertEquals(2, result.words().size());
        assertTrue(result.words().stream().anyMatch(wordDto -> wordDto.japaneseWord().equals("依頼(する)")));
        assertTrue(result.words().stream().anyMatch(wordDto -> wordDto.japaneseWord().equals("隠れる")));
        assertTrue(result.words().stream().anyMatch(wordDto -> wordDto.englishWord().size() == 3));
        assertTrue(result.words().stream().anyMatch(wordDto -> wordDto.hiragana().equals("いらい")));
        assertTrue(result.words().stream().anyMatch(wordDto -> wordDto.hiragana().equals("かくれる")));
        assertTrue(result.words().stream().anyMatch(wordDto -> wordDto.categories().size() == 2));
        assertTrue(result.words().stream().anyMatch(wordDto -> wordDto.categories().size() == 3));
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException as category with given ID not found when requesting CategoryDto")
    void getCategoryDtoById_throwsExceptionAsNoCategoryFound() {
        when(categoryRepository.findById(2L)).thenThrow(new CategoryNotFoundException("Category not found TEST."));

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(2L));
    }

    @Test
    @DisplayName("Should successfully return a Category by ID")
    void getCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(getCategory()));

        Category result = categoryService.getCategoryById(1L);

        assertNotNull(result);

        assertEquals(1L, result.getId());
        assertEquals("verb", result.getName());
        assertEquals(2, result.getWords().size());
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException as category with given ID not found when requesting Category")
    void getCategoryById_throwsCategoryNotFoundException() {
        when(categoryRepository.findById(2L)).thenThrow(new CategoryNotFoundException("Category not found TEST."));

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(2L));
    }

    @Test
    void getCategoryByName() {
    }

    @Test
    void getAllCategories() {
    }

    @Test
    void addCategory() {
    }

    @Test
    void addWordToCategoryMapping() {
    }

    @Test
    void deleteCategoryById() {
    }

    @Test
    void deleteCategoryByName() {
    }

    @Test
    void checkIfCategoryExistsByName() {
    }

    @Test
    void updateCategories() {
    }

    @Test
    void dealWithCategories() {
    }

    @Test
    void removeWordFromCategory() {
    }

    private Category getCategory() {
        Collection<Word> words = new ArrayList<>();
        words.add(iraiSuruWord());
        words.add(kakuReRuWord());

        return new Category(1L, "verb", words);
    }

    private Word iraiSuruWord() {
        Set<Category> wordCategories = new HashSet<>();
        wordCategories.add(new Category("verb"));
        wordCategories.add(new Category("noun"));
        wordCategories.add(new Category("transitive"));
        return new Word("依頼(する)",
                List.of(new EnglishWord("to request"), new EnglishWord("to commission"), new EnglishWord("to entrust (with a matter)")),
                "いらい",
                wordCategories);
    }

    private Word kakuReRuWord() {
        Set<Category> wordCategories = new HashSet<>();
        wordCategories.add(new Category("verb"));
        wordCategories.add(new Category("noun"));
        return new Word("隠れる",
                List.of(new EnglishWord("to hide"), new EnglishWord("to conceal oneself"), new EnglishWord("to disappear (behind)")),
                "かくれる",
                wordCategories);
    }

//    private CategoryDto getCategoryDto(){
//        return new CategoryDto(1L, "verb", getWordDto());
//    }
//
//    private List<WordDto> getWordDto(){
//        WordDto iraiSuru = new WordDto(1L, "依頼(する)", List.of("to request", "to commission", "to entrust (with a matter)"), "いらい", List.of("verb", "noun", "transitive"));
//        WordDto kakuReRu = new WordDto(2L, "隠れる", List.of("to hide", "to conceal oneself", "to disappear (behind)"), "かくれる", List.of("verb", "hide"));
//
//        return List.of(iraiSuru, kakuReRu);
//    }

    private WordDto iraiSuruWordDto() {
        return new WordDto(1L,
                "依頼(する)",
                List.of("to request", "to commission", "to entrust (with a matter)"),
                "いらい",
                List.of("verb", "noun", "transitive"));
    }

    private WordDto kakuReRuWordDto() {
        return new WordDto(2L,
                "隠れる",
                List.of("to hide", "to conceal oneself", "to disappear (behind)"),
                "かくれる",
                List.of("verb", "hide"));
    }
}