package com.japanese.study_app.service.exampleSentence;

import com.japanese.study_app.model.ExampleSentence;

import java.util.List;

public interface IExampleSentenceService {

    List<ExampleSentence> getAllExampleSentences();

    void deleteExampleSentenceById(Long id);

}
