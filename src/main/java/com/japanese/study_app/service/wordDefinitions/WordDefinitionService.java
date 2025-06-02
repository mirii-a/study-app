package com.japanese.study_app.service.wordDefinitions;

import com.japanese.study_app.model.Word;
import com.japanese.study_app.model.WordDefinition;
import com.japanese.study_app.repository.WordDefinitionRepository;
import com.japanese.study_app.request.RequestWordDefinitions;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class WordDefinitionService {

    private final WordDefinitionRepository wordDefinitionRepository;

    public WordDefinitionService(WordDefinitionRepository wordDefinitionRepository) {
        this.wordDefinitionRepository = wordDefinitionRepository;
    }

    public void dealWithDefinitions(Word word, RequestWordDefinitions definitions) {
        // Updates the Word object so no need to return anything
        WordDefinition wordDefinition = new WordDefinition();

        if (definitions != null) {
            // Get Japanese definitions and make them into one string
            if (wordDefinitionRepository.findByWord(word) != null) {
                wordDefinition = wordDefinitionRepository.findByWord(word);
            }

            String japaneseDefinitions;
            if (definitions.japanese() != null) {
                japaneseDefinitions = buildStringForDefinition(definitions.japanese());
            } else {
                japaneseDefinitions = "";
            }
            wordDefinition.setDefinitionJapanese(japaneseDefinitions);

            String englishDefinitions;
            if (definitions.english() != null) {
                englishDefinitions = buildStringForDefinition(definitions.english());
            } else {
                englishDefinitions = "";
            }
            wordDefinition.setDefinitionEnglish(englishDefinitions);

            wordDefinition.setWord(word);

            wordDefinitionRepository.save(wordDefinition);

            WordDefinition wordDefinitionsForWord = wordDefinitionRepository.findByWord(word);
            word.setDefinitions(wordDefinitionsForWord);
        } else {
            // Set as blank list as there are no definitions
            // Nothing to save to the database
            wordDefinition.setDefinitionEnglish("");
            wordDefinition.setDefinitionJapanese("");
            word.setDefinitions(wordDefinition);
        }
    }

    public void removeDefinitionsForWord(Word word) {
        Optional.ofNullable(word.getDefinitions()).ifPresent(wordDefinitionRepository::delete);
    }

    private String buildStringForDefinition(Set<String> definitions) {
        StringBuilder definitionStringBuilder = new StringBuilder();

        if (definitions != null) {
            for (String definition : definitions) {
                definitionStringBuilder.append(definition).append(";");
            }
        }

        return definitionStringBuilder.toString();
    }
}
