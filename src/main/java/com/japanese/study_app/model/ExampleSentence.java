package com.japanese.study_app.model;

import java.util.Collection;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExampleSentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String japaneseSentence;
//    private String hiraganaSentence;
    private String englishSentence;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "example_sentences_for_words", joinColumns = @JoinColumn(name = "example_sentence_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(name = "word_id", referencedColumnName = "id")
    )
    private Collection<Word> words = new HashSet<>();

    // Add Category to ExampleSentence

}
