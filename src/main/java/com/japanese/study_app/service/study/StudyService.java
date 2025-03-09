package com.japanese.study_app.service.study;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.StudyRepository;
import com.japanese.study_app.service.word.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudyService implements IStudyService{

    private final StudyRepository studyRepository;
    private final WordService wordService;

    @Override
    public List<WordDto> getRandomWords() {
        List<Word> randomWords = studyRepository.findRandomWords();
        return randomWords.stream().map(wordService::convertWordToDto).toList();
    }

    @Override
    public List<WordDto> getNumberOfRandomWords(Long numberOfWordsToReturn) {
        List<Word> randomWords = studyRepository.findNumberOfRandomWords(numberOfWordsToReturn);
        return randomWords.stream().map(wordService::convertWordToDto).toList();
    }

    @Override
    public List<WordDto> getRandomWordsByCategory(String category) {
        List<Word> randomWordsByCategory = studyRepository.findNumberOfRandomWordsBasedOnCategory(category, Long.valueOf(10));
        return randomWordsByCategory.stream().map(wordService::convertWordToDto).toList();
    }

    @Override
    public List<WordDto> getNumberOfRandomWordsByCategory(String category, Long numberOfWordsToReturn) {
        List<Word> randomWordsByCategory = studyRepository.findNumberOfRandomWordsBasedOnCategory(category, numberOfWordsToReturn);
        return randomWordsByCategory.stream().map(wordService::convertWordToDto).toList();
    }
}
