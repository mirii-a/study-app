// package com.japanese.study_app.model;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;

// @Entity
// public class WordDefinition {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String definitionJapanese;
//     private String definitionEnglish;

//     @ManyToOne
//     @JoinColumn(name= "word_id")
//     private Word word;
// }
