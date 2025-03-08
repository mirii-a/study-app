package com.japanese.study_app.service.exampleSentence;

import com.japanese.study_app.exceptions.ExampleSentenceNotFoundException;
import com.japanese.study_app.model.ExampleSentence;
import com.japanese.study_app.repository.ExampleSentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExampleSentenceService implements IExampleSentenceService{

    private final ExampleSentenceRepository exampleSentenceRepository;

    @Override
    public List<ExampleSentence> getAllExampleSentences() {
        return exampleSentenceRepository.findAll();
    }

    @Override
    public void deleteExampleSentenceById(Long id) {
        Optional<ExampleSentence> exampleToDelete = exampleSentenceRepository.findById(id);
        exampleToDelete.ifPresentOrElse(exampleSentenceRepository::delete,
                () -> {throw new ExampleSentenceNotFoundException("Example sentence not found in database.");});
    }
}
