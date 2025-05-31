package com.japanese.study_app.request;

import com.japanese.study_app.model.Category;
import com.japanese.study_app.model.EnglishWord;

import java.util.Map;
import java.util.Set;

public record UpdateWordRequest(
        Long id,
        String japaneseWord,
        Set<EnglishWord> englishWord,
        String hiragana,
        Set<Map<String, String>> exampleSentence,
        Map<String, Set<String>> definitions,
        Set<Category> category
) {

}
