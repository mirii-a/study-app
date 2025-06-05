package com.japanese.study_app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordTest {

    Word word;

    @BeforeEach
    void setUp() {
        Collection<EnglishWord> englishWords = new ArrayList<>();
        englishWords.add(new EnglishWord("stereotype"));
        englishWords.add(new EnglishWord("prejudice"));
        englishWords.add(new EnglishWord("fixed idea"));

        Set<Category> categories = new HashSet<>();
        categories.add(new Category("noun"));
        categories.add(new Category("opinion"));

        word = new Word("固定観念", englishWords, "こていかんねん", categories);
    }

    @Test
    @DisplayName("Should construct Word object correctly")
    void constructWordObject() {
        assertEquals("固定観念", word.getJapaneseWord());
        assertEquals("こていかんねん", word.getHiragana());

        assertTrue(word.getEnglishWord().stream().anyMatch(english -> english.getEnglishWord().equals("stereotype")));
        assertTrue(word.getEnglishWord().stream().anyMatch(english -> english.getEnglishWord().equals("prejudice")));

        assertTrue(word.getCategory().stream().anyMatch(category -> category.getName().equals("noun")));
        assertTrue(word.getCategory().stream().anyMatch(category -> category.getName().equals("opinion")));
    }

    @Test
    @DisplayName("Testing getId method for Word object")
    void getId() {
        // Null as not been added to the database
        assertNull(word.getId());
    }

    @Test
    @DisplayName("Testing getJapaneseWord method for Word object")
    void getJapaneseWord() {
        assertEquals("固定観念", word.getJapaneseWord());
    }

    @Test
    void getHiragana() {
    }

    @Test
    void getEnglishWord() {
    }

    @Test
    void getExampleSentences() {
    }

    @Test
    void getDefinitions() {
    }

    @Test
    void getCategory() {
    }

    @Test
    void setEnglishWord() {
    }

    @Test
    void setHiragana() {
    }

    @Test
    void setJapaneseWord() {
    }

    @Test
    void setExampleSentences() {
    }

    @Test
    void setDefinitions() {
    }

    @Test
    void setCategory() {
    }
}