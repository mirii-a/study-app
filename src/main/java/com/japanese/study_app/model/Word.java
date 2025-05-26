package com.japanese.study_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    private String japaneseWord;
    private String hiragana;

    @ManyToMany(mappedBy = "word")
    private Collection<EnglishWord> englishWord = new HashSet<>();

    @ManyToMany(mappedBy = "words")
    private Collection<ExampleSentence> exampleSentences = new HashSet<>();

    // when the word is deleted, so too will the definitions
    @OneToOne(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
    private WordDefinition definitions;

    @ManyToMany(mappedBy = "words")
    @JsonIgnore
    private Collection<Category> category = new HashSet<>();

    public Word() {
    } //default constructor

    public Word(String japaneseWord, Collection<EnglishWord> englishWord, String hiragana, Set<Category> categories) {
        this.japaneseWord = japaneseWord;
        this.englishWord = englishWord;
        this.hiragana = hiragana;
        this.category.addAll(categories);
    }

    public Word(String japaneseWord, Collection<EnglishWord> englishWord, String hiragana) {
        this.japaneseWord = japaneseWord;
        this.englishWord = englishWord;
        this.hiragana = hiragana;
    }

    public Long getId() {
        return this.id;
    }

    public String getJapaneseWord() {
        return this.japaneseWord;
    }

    public String getHiragana() {
        return this.hiragana;
    }

    public Collection<EnglishWord> getEnglishWord() {
        return this.englishWord;
    }

    public Collection<ExampleSentence> getExampleSentences() {
        return this.exampleSentences;
    }

    public WordDefinition getDefinitions() {
        return this.definitions;
    }

    public Collection<Category> getCategory() {
        return this.category;
    }

    public void setEnglishWord(Collection<EnglishWord> englishWord) {
        this.englishWord = englishWord;
    }

    public void setHiragana(String hiragana) {
        this.hiragana = hiragana;
    }

    public void setJapaneseWord(String japaneseWord) {
        this.japaneseWord = japaneseWord;
    }

    public void setExampleSentences(Collection<ExampleSentence> exampleSentences) {
        this.exampleSentences = exampleSentences;
    }

    public void setDefinitions(WordDefinition wordDefinition) {
        this.definitions = wordDefinition;
    }

    public void setCategory(Collection<Category> categories) {
        this.category = categories;
    }
}
