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

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "categories_for_words", joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "word_id", referencedColumnName = "id")
    )
    private Collection<Word> words = new HashSet<>(); // i don't think the category needs this

    public Category() {} //default constructor

    public Category(String name) {
        // this is needed so we can use 'category.getName()'
        this.name = name;
    }

    public Category(Long id, String name, Collection<Word> words) {
        this.id = id;
        this.name = name;
        this.words = words;
    }

    public Long getId(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Collection<Word> getWords() {
        return this.words;
    }
}
