package com.japanese.study_app.request;

import com.japanese.study_app.model.Category;
import com.japanese.study_app.model.EnglishWord;

import java.util.Set;

public record UpdateWordRequest(
        Long id,
        String japaneseWord,
        Set<EnglishWord> englishWord,
        String hiragana,
        Set<RequestWordExampleSentences> exampleSentence,
        RequestWordDefinitions definitions,
        Set<Category> category
) {

}
