package com.japanese.study_app.service.study;

import com.japanese.study_app.dto.WordDto;

import java.util.List;

public interface IStudyService {
    List<WordDto> getRandomWords();
    List<WordDto> getNumberOfRandomWords(Long numberOfWordsToReturn);
    List<WordDto> getRandomWordsByCategory(String category);

}
