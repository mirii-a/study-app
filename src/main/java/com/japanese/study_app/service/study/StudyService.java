package com.japanese.study_app.service.study;

import com.japanese.study_app.dto.WordDto;

import java.util.List;

public class StudyService implements IStudyService{
    @Override
    public List<WordDto> getRandomWordSet(Long numberOfWordsToReturn) {
        return List.of();
    }

    @Override
    public List<WordDto> getRandomWordsByCategory(String category) {
        return List.of();
    }
}
