package com.japanese.study_app.service.study;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.StudyRepository;
import com.japanese.study_app.service.word.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudyService implements IStudyService{

    private final StudyRepository studyRepository;
    private final WordService wordService;

    @Override
    public List<WordDto> getRandomWordSet() {
        List<Word> randomWords = studyRepository.findRandomWords();
        return randomWords.stream().map(wordService::convertWordToDto).toList();
    }

    @Override
    public List<WordDto> getRandomWordsByCategory(String category) {
        return List.of();
    }
}
