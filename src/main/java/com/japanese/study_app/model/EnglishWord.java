package com.japanese.study_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Entity;

import java.util.Collection;
import java.util.HashSet;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public EnglishWord(String englishWord){
        this.englishWord = englishWord;
    }
}
