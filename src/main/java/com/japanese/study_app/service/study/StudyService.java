package com.japanese.study_app.service.study;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.StudyRepository;
import com.japanese.study_app.service.word.WordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyService implements IStudyService {

    private final StudyRepository studyRepository;
    private final WordService wordService;

    public StudyService(StudyRepository studyRepository, WordService wordService) {
        this.studyRepository = studyRepository;
        this.wordService = wordService;
    }

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
        List<Word> randomWordsByCategory = studyRepository.findNumberOfRandomWordsBasedOnCategory(category, 10L);
        return randomWordsByCategory.stream().map(wordService::convertWordToDto).toList();
    }

    @Override
    public List<WordDto> getNumberOfRandomWordsByCategory(String category, Long numberOfWordsToReturn) {
        List<Word> randomWordsByCategory = studyRepository.findNumberOfRandomWordsBasedOnCategory(category, numberOfWordsToReturn);
        return randomWordsByCategory.stream().map(wordService::convertWordToDto).toList();
    }

    @Override
    public List<WordDto> getRandomWordsByEnglishWord(String englishWord) {
        List<Word> randomWordsByEnglish = studyRepository.findNumberOfRandomWordsBasedOnEnglish(englishWord, 10L);
        return randomWordsByEnglish.stream().map(wordService::convertWordToDto).toList();
    }
}
