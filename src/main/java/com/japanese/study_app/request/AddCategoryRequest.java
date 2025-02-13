package com.japanese.study_app.request;

import com.japanese.study_app.model.Category;
import com.japanese.study_app.model.EnglishWord;
import lombok.Data;

import java.util.Set;

@Data
public class AddCategoryRequest {
    private Long id;
    private String name;
}
