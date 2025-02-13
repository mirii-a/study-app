package com.japanese.study_app.request;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.japanese.study_app.model.Category;
// import com.japanese.study_app.model.ExampleSentence;
// import com.japanese.study_app.model.WordDefinition;
import com.japanese.study_app.model.EnglishWord;
import lombok.Data;

@Data
public class AddWordRequest {
    private Long id;
    private String japaneseWord;
    private Set<EnglishWord> englishWord;
    private String hiragana;
    // private Collection<ExampleSentence> exampleSentences;
    // private Collection<WordDefinition> definitions;
    private Set<Category> category;
}
