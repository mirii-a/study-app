package com.japanese.study_app.dto;

import java.util.List;
import java.util.Map;

public record WordDto(
        Long id,
        String japaneseWord,
        List<String> englishWord,
        String hiragana,
        List<String> categories,
        List<String> englishDefinitions,
        List<String> japaneseDefinitions,
        List<Map<String, String>> exampleSentences
) {

    public WordDto {}

    public WordDto(Long id, String japaneseWord, List<String> englishWord, String hiragana, List<String> categories) {
        this(id, japaneseWord, englishWord, hiragana, categories, List.of(), List.of(), List.of());
    }

    public WordDto updateExampleSentences(List<Map<String,String>> newExampleSentences) {
        return new WordDto(id, japaneseWord, englishWord, hiragana, categories, englishDefinitions, japaneseDefinitions, newExampleSentences);
    }

    public WordDto updateEnglishDefinitions(List<String> newEnglishDefinitions) {
        return new WordDto(id, japaneseWord, englishWord, hiragana, categories, newEnglishDefinitions, japaneseDefinitions, exampleSentences);

    }

    public WordDto updateJapaneseDefinitions(List<String> newJapaneseDefinitions) {
        return new WordDto(id, japaneseWord, englishWord, hiragana, categories, englishDefinitions, newJapaneseDefinitions, exampleSentences);
    }

}
