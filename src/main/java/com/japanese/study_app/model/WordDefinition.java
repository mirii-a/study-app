 package com.japanese.study_app.model;

 import jakarta.persistence.*;
 import lombok.AllArgsConstructor;
 import lombok.Getter;
 import lombok.NoArgsConstructor;
 import lombok.Setter;

 import java.util.List;

 @Entity
 @Getter
 @Setter
 @AllArgsConstructor
 @NoArgsConstructor
 public class WordDefinition {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private String definitionJapanese;
     private String definitionEnglish;

     @OneToOne
     @JoinColumn(name= "word_id")
     private Word word;
 }
