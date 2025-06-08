package com.japanese.study_app.service.study;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.exceptions.RandomWordRetrievalException;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.StudyRepository;
import com.japanese.study_app.service.word.WordDtoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyService implements IStudyService {

    private final StudyRepository studyRepository;
    private final WordDtoService wordDtoService;

    public StudyService(StudyRepository studyRepository, WordDtoService wordDtoService) {
        this.studyRepository = studyRepository;
        this.wordDtoService = wordDtoService;
    }

    @Override
    public List<WordDto> getRandomWords() {
        try {
            List<Word> randomWords = studyRepository.findRandomWords();
            return randomWords.stream().map(wordDtoService::convertWordToDto).toList();
        } catch (Exception e) {
            throw new RandomWordRetrievalException("Retrieval of random words unsuccessful. Error: " + e.getMessage());
        }
    }

    @Override
    public List<WordDto> getNumberOfRandomWords(Long numberOfWordsToReturn) {
        try {
            List<Word> randomWords = studyRepository.findNumberOfRandomWords(numberOfWordsToReturn);
            return randomWords.stream().map(wordDtoService::convertWordToDto).toList();
        } catch (Exception e) {
            throw new RandomWordRetrievalException("Retrieval of " + numberOfWordsToReturn.toString()
                    + " random words unsuccessful. Error: " + e.getMessage());
        }

    }

    @Override
    public List<WordDto> getRandomWordsByCategory(String category) {
        try {
            List<Word> randomWordsByCategory = studyRepository
                    .findNumberOfRandomWordsBasedOnCategory(category, 10L);
            return randomWordsByCategory.stream().map(wordDtoService::convertWordToDto).toList();
        } catch (Exception e) {
            throw new RandomWordRetrievalException("Retrieval of random words by category " + category
                    + " unsuccessful. Error: " + e.getMessage());
        }
    }

    @Override
    public List<WordDto> getNumberOfRandomWordsByCategory(String category, Long numberOfWordsToReturn) {
        try {
            List<Word> randomWordsByCategory = studyRepository
                    .findNumberOfRandomWordsBasedOnCategory(category, numberOfWordsToReturn);
            return randomWordsByCategory.stream().map(wordDtoService::convertWordToDto).toList();
        } catch (Exception e) {
            throw new RandomWordRetrievalException("Retrieval of " + numberOfWordsToReturn.toString() +
                    " random words by category " + category + " unsuccessful. Error: " + e.getMessage());
        }
    }

    @Override
    public List<WordDto> getRandomWordsByEnglishWord(String englishWord) {
        try {
            List<Word> randomWordsByEnglish = studyRepository
                    .findNumberOfRandomWordsBasedOnEnglish(englishWord, 10L);
            return randomWordsByEnglish.stream().map(wordDtoService::convertWordToDto).toList();
        } catch (Exception e) {
            throw new RandomWordRetrievalException("Retrieval of random words by English word " + englishWord
                    + " unsuccessful. Error: " + e.getMessage());
        }
    }

    @Override
    public List<WordDto> getNumberOfRandomWordsByEnglishWord(String englishWord, Long numberOfWordsToReturn) {
        try {
            List<Word> randomWordsByEnglish = studyRepository
                    .findNumberOfRandomWordsBasedOnEnglish(englishWord, 10L);
            return randomWordsByEnglish.stream().map(wordDtoService::convertWordToDto).toList();
        } catch (Exception e) {
            throw new RandomWordRetrievalException("Retrieval of " + numberOfWordsToReturn.toString() +
                    " random words by English word " + englishWord + " unsuccessful. Error: " + e.getMessage());
        }
    }
}
