package com.japanese.study_app.request;

import java.util.Map;
import java.util.Set;

import com.japanese.study_app.model.Category;
// import com.japanese.study_app.model.ExampleSentence;
import com.japanese.study_app.model.EnglishWord;

import com.japanese.study_app.model.ExampleSentence;
import lombok.Data;

@Data
public class AddWordRequest {
    private Long id;
    private String japaneseWord;
    private Set<EnglishWord> englishWord;
    private String hiragana;
    private Set<ExampleSentence> exampleSentences;
    private Map<String, Set<String>> definitions;
    private Set<Category> category;
}
