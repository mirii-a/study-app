package com.japanese.study_app.dto;

import com.japanese.study_app.model.Category;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Data
public class WordDto {

    private Long id;
    private String japaneseWord;
    private List<String> englishWord;
    private String hiragana;
    private List<String> categories;
}
