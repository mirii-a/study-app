package com.japanese.study_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import java.util.Collection;
import java.util.HashSet;

@Entity
public class ExampleSentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String japaneseSentence;
    // TODO:  private String hiraganaSentence;
    private String englishSentence;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "example_sentences_for_words", joinColumns = @JoinColumn(name = "example_sentence_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "word_id", referencedColumnName = "id")
    )
    private Collection<Word> words = new HashSet<>();

    // TODO: Add Category to ExampleSentence

    public Long getId() {
        return id;
    }

    public String getJapaneseSentence() {
        return japaneseSentence;
    }

    public String getEnglishSentence() {
        return englishSentence;
    }

    public Collection<Word> getWords() {
        return words;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJapaneseSentence(String japaneseSentence) {
        this.japaneseSentence = japaneseSentence;
    }

    public void setEnglishSentence(String englishSentence) {
        this.englishSentence = englishSentence;
    }

    public void setWords(Collection<Word> words) {
        this.words = words;
    }
}
