package com.japanese.study_app.request;

import java.util.Collection;

import com.japanese.study_app.model.Category;
import com.japanese.study_app.model.ExampleSentence;
// import com.japanese.study_app.model.WordDefinition;

import lombok.Data;

@Data
public class UpdateWordRequest {
    private Long id;
    private String japaneseWord;
    private String englishWord;
    private String hiragana;
    private Collection<ExampleSentence> exampleSentences;
    // private Collection<WordDefinition> definitions;
    private Collection<Category> category;
}
