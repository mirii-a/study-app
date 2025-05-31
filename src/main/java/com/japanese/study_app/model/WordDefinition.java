package com.japanese.study_app.model;

import jakarta.persistence.*;

@Entity
public class WordDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String definitionJapanese;
    private String definitionEnglish;

    @OneToOne
    @JoinColumn(name = "word_id")
    private Word word;

    public Long getId() {
        return this.id;
    }

    public String getDefinitionJapanese() {
        return this.definitionJapanese;
    }

    public String getDefinitionEnglish() {
        return this.definitionEnglish;
    }

    public Word getWord() {
        return this.word;
    }

    public void setDefinitionJapanese(String definitionJapanese) {
        this.definitionJapanese = definitionJapanese;
    }

    public void setDefinitionEnglish(String definitionEnglish) {
        this.definitionEnglish = definitionEnglish;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
