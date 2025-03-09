package com.japanese.study_app.service.study;

import com.japanese.study_app.dto.WordDto;

import java.util.List;

public interface IStudyService {
    List<WordDto> getRandomWordSet(Long numberOfWordsToReturn);
    List<WordDto> getRandomWordsByCategory(String category);

}
