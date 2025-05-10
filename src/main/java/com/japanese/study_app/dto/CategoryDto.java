package com.japanese.study_app.dto;

import java.util.List;

public record CategoryDto(
        Long id,
        String name,
        List<WordDto> words) {
}
