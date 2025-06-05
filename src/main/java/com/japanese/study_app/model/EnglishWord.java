package com.japanese.study_app.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;


@Entity
public class EnglishWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String englishWord;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "english_words_for_words", joinColumns = @JoinColumn(name = "english_word_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "word_id", referencedColumnName = "id")
    )
    private Collection<Word> word = new HashSet<>();

    public EnglishWord() {
    }

    public EnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public Long getId() {
        return this.id;
    }

    public String getEnglishWord() {
        return this.englishWord;
    }

    public Collection<Word> getWord() {
        return this.word;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public void setWord(Collection<Word> words) {
        this.word = words;
    }
}
