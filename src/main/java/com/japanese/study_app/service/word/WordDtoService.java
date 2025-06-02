package com.japanese.study_app.service.word;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordDtoService {

    public WordDtoService() {

    }

    public WordDto convertWordToDto(Word word) {
        WordDto wordDto = new WordDto(
                word.getId(),
                word.getJapaneseWord(),
                word.getEnglishWord().stream().map(EnglishWord::getEnglishWord).collect(Collectors.toList()),
                word.getHiragana(),
                word.getCategory().stream().map(Category::getName).collect(Collectors.toList())
        );

        if (word.getDefinitions() != null) {
            WordDefinition definitionsForWord = word.getDefinitions();

            List<String> allEnglishDefinitions = convertDefinitionsToListOfStringsForDtoOutput(
                    definitionsForWord.getDefinitionEnglish().split(";"));
            List<String> allJapaneseDefinitions = convertDefinitionsToListOfStringsForDtoOutput(
                    definitionsForWord.getDefinitionJapanese().split(";"));

            wordDto = setDefinitionsForWordDto(wordDto, allEnglishDefinitions, allJapaneseDefinitions);
        } else {
            List<String> blankList = new ArrayList<>();
            wordDto = setDefinitionsForWordDto(wordDto, blankList, blankList);
        }

        if (word.getExampleSentences() != null) {
            Collection<ExampleSentence> exampleSentences = word.getExampleSentences();
            List<Map<String, String>> examplesForWord = new ArrayList<>();
            for (ExampleSentence sentence : exampleSentences) {
                Map<String, String> example = new HashMap<>();
                example.put("englishSentence", sentence.getEnglishSentence());
                example.put("japaneseSentence", sentence.getJapaneseSentence());
                examplesForWord.add(example);
            }
            wordDto = wordDto.updateExampleSentences(examplesForWord);
        } else {
            List<Map<String, String>> blankList = new ArrayList<>();
            wordDto = wordDto.updateExampleSentences(blankList);
        }
        return wordDto;
    }

    private List<String> convertDefinitionsToListOfStringsForDtoOutput(String[] definitions) {
        List<String> listOfDefinitions = new ArrayList<>();
        listOfDefinitions.addAll(Arrays.stream(definitions).toList());
        return listOfDefinitions;
    }

    private WordDto setDefinitionsForWordDto(WordDto wordDto, List<String> englishDefinitions, List<String> japaneseDefinitions) {
        wordDto = wordDto.updateEnglishDefinitions(englishDefinitions);
        wordDto = wordDto.updateJapaneseDefinitions(japaneseDefinitions);
        return wordDto;
    }

}
