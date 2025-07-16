package com.japanese.study_app.model;

import com.japanese.study_app.testObjects.TestObjectsWord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WordTest {

    Word word;

    private final TestObjectsWord helper = new TestObjectsWord();

    @BeforeEach
    void setUp() {
        word = helper.getKoteiKanNen();
    }

    @Test
    @DisplayName("Should construct Word object correctly")
    void constructWordObject() {
        assertEquals("固定観念", word.getJapaneseWord());
        assertEquals("こていかんねん", word.getHiragana());
        assertThat(word.getClass()).isEqualTo(Word.class);
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
        assertEquals("こていかんねん", word.getHiragana());
    }

    @Test
    void getEnglishWord() {
        assertTrue(word.getEnglishWord().stream().anyMatch(engWord -> engWord.getEnglishWord().equals("stereotype")));
        assertTrue(word.getEnglishWord().stream().anyMatch(engWord -> engWord.getEnglishWord().equals("prejudice")));
        assertTrue(word.getEnglishWord().stream().anyMatch(engWord -> engWord.getEnglishWord().equals("fixed idea")));
    }

//    @Test
//    void getExampleSentences() {
//    }

    @Test
    void getDefinitions() {
        assertEquals("An idea that is always stuck in one's head and restricts one's thinking;A way of thinking in which certain ideas or values become rigid and restrict one's thinking", word.getDefinitions().getDefinitionEnglish());
        assertEquals("いつも頭から離れないで、その人の思考を拘束するような考え;特定の考えや価値観が凝り固まり、思考を束縛するような考え方", word.getDefinitions().getDefinitionJapanese());
    }

    @Test
    void getCategory() {
        assertTrue(word.getCategory().stream().anyMatch(category -> category.getName().equals("noun")));
        assertTrue(word.getCategory().stream().anyMatch(category -> category.getName().equals("opinion")));
    }

//    @Test
//    void setEnglishWord() {
//    }
//
//    @Test
//    void setHiragana() {
//    }
//
//    @Test
//    void setJapaneseWord() {
//    }
//
//    @Test
//    void setExampleSentences() {
//    }
//
//    @Test
//    void setDefinitions() {
//    }
//
//    @Test
//    void setCategory() {
//    }
}