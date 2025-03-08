package com.japanese.study_app.service.exampleSentence;

import com.japanese.study_app.model.ExampleSentence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExampleSentenceService implements IExampleSentenceService{

    @Override
    public List<ExampleSentence> getAllExampleSentences() {
        return List.of();
    }

    @Override
    public void deleteExampleSentenceById(Long id) {

    }
}
