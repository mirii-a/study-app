package com.japanese.study_app.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Word {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id; // primary key

    private String japaneseWord;
    private String hiragana;

    @ManyToMany(mappedBy = "word")
    private Collection<EnglishWord> englishWord = new HashSet<>();

    // @ManyToMany(mappedBy = "words")
    // private Collection<ExampleSentence> exampleSentences = new HashSet<>();

    // when the word is deleted, so too will the definitions
     @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<WordDefinition> definitions;

    @ManyToMany(mappedBy = "words")
    @JsonIgnore
    private Collection<Category> category = new HashSet<>();

    public Word(String japaneseWord, Collection<EnglishWord> englishWord, String hiragana, Set<Category> categories){
        this.japaneseWord = japaneseWord;
        this.englishWord = englishWord;
        this.hiragana = hiragana;
        this.category.addAll(categories);
    }

    public Word(String japaneseWord, Collection<EnglishWord> englishWord, String hiragana){
        this.japaneseWord = japaneseWord;
        this.englishWord = englishWord;
        this.hiragana = hiragana;
    }
}
