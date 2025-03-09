package com.japanese.study_app.dto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WordDto {

    private Long id;
    private String japaneseWord;
    private List<String> englishWord;
    private String hiragana;
    private List<String> categories;
    private List<String> englishDefinitions;
    private List<String> japaneseDefinitions;
    private List<Map<String,String>> exampleSentences;
}
