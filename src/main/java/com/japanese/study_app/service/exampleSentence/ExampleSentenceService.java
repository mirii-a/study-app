package com.japanese.study_app.service.exampleSentence;

import com.japanese.study_app.exceptions.ExampleSentenceNotFoundException;
import com.japanese.study_app.model.ExampleSentence;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.ExampleSentenceRepository;
import com.japanese.study_app.request.RequestWordExampleSentences;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExampleSentenceService implements IExampleSentenceService {

    private final ExampleSentenceRepository exampleSentenceRepository;

    public ExampleSentenceService(ExampleSentenceRepository exampleSentenceRepository) {
        this.exampleSentenceRepository = exampleSentenceRepository;
    }

    @Override
    public List<ExampleSentence> getAllExampleSentences() {
        return exampleSentenceRepository.findAll();
    }

    @Override
    public void deleteExampleSentenceById(Long id) {
        Optional<ExampleSentence> exampleToDelete = exampleSentenceRepository.findById(id);
        exampleToDelete.ifPresentOrElse(exampleSentenceRepository::delete,
                () -> {
                    throw new ExampleSentenceNotFoundException("Example sentence not found in database.");
                });
    }

    public void deleteExampleSentences(Word word) {
        Optional.ofNullable(word.getExampleSentences())
                .ifPresent(sentences -> sentences.forEach(exampleSentenceRepository::delete));
    }

    public void dealWithExampleSentences(Word word, Set<RequestWordExampleSentences> exampleSentences) {
        // TODO: Add better validation when wanting to update a word. Separate out this logic
        if (exampleSentences != null) {
            exampleSentences.forEach(example -> {
                if (!Objects.equals(example.japaneseSentence(), "") && example.japaneseSentence() != null) {
                    if (exampleSentenceRepository.existsByJapaneseSentence(example.japaneseSentence())) {
                        ExampleSentence sentenceExample = new ExampleSentence();
                        Optional<ExampleSentence> sentenceAlreadyExisting = exampleSentenceRepository.findByJapaneseSentence(example.japaneseSentence());
                        sentenceAlreadyExisting.ifPresent(value -> {
                            if (!value.getEnglishSentence().equals(example.englishSentence())) {
                                sentenceExample.setJapaneseSentence(value.getJapaneseSentence());
                                sentenceExample.setEnglishSentence(example.englishSentence());
                                sentenceExample.setId(value.getId());
                            } else {
                                sentenceExample.setJapaneseSentence(value.getJapaneseSentence());
                                sentenceExample.setEnglishSentence(value.getEnglishSentence());
                                sentenceExample.setId(value.getId());
                            }
                            Collection<Word> words = value.getWords();

                            if (!example.englishSentence().equals(value.getEnglishSentence())) {
                                value.setEnglishSentence(example.englishSentence());
                                if (!words.contains(word)) {
                                    words.add(word);
                                    value.setWords(words);
                                }
                                exampleSentenceRepository.save(value);
                            }

                            Collection<ExampleSentence> exampleSentencesForWord = exampleSentenceRepository.findByWords(word);
                            word.setExampleSentences(exampleSentencesForWord);
                        });
                    } else if (exampleSentenceRepository.existsByEnglishSentence(example.englishSentence()) && !exampleSentenceRepository.existsByJapaneseSentence(example.japaneseSentence())) {
                        ExampleSentence sentenceExample = new ExampleSentence();
                        Optional<ExampleSentence> sentenceAlreadyExisting = exampleSentenceRepository.findByEnglishSentence(example.englishSentence());
                        sentenceAlreadyExisting.ifPresent(value -> {
                            if (!value.getJapaneseSentence().equals(example.japaneseSentence())) {
                                sentenceExample.setJapaneseSentence(example.japaneseSentence());
                                sentenceExample.setEnglishSentence(value.getEnglishSentence());
                                sentenceExample.setId(value.getId());
                            } else {
                                sentenceExample.setJapaneseSentence(value.getJapaneseSentence());
                                sentenceExample.setEnglishSentence(value.getEnglishSentence());
                                sentenceExample.setId(value.getId());
                            }
                            Collection<Word> words = value.getWords();

                            if (!example.japaneseSentence().equals(value.getJapaneseSentence())) {
                                value.setJapaneseSentence(example.japaneseSentence());
                                if (!words.contains(word)) {
                                    words.add(word);
                                    value.setWords(words);
                                }
                                exampleSentenceRepository.save(value);
                            }
                            Collection<ExampleSentence> exampleSentencesForWord = exampleSentenceRepository.findByWords(word);
                            word.setExampleSentences(exampleSentencesForWord);
                        });
                    } else {
                        ExampleSentence sentenceExample = new ExampleSentence();
                        sentenceExample.setEnglishSentence(example.englishSentence());
                        sentenceExample.setJapaneseSentence(example.japaneseSentence());
                        Collection<Word> words = new HashSet<>();
                        // Add word to mapping for this new example sentence
                        words.add(word);
                        sentenceExample.setWords(words);
                        // save new example to repository
                        exampleSentenceRepository.save(sentenceExample);

                        Collection<ExampleSentence> exampleSentencesForWord = exampleSentenceRepository.findByWords(word);
                        word.setExampleSentences(exampleSentencesForWord);
                    }
                } else {
                    // Set as blank list as there are no definitions
                    setBlankExampleSentence(word);
                }
            });
        } else {
            // Set as blank list as there are no definitions
            setBlankExampleSentence(word);
        }
    }

    private void setBlankExampleSentence(Word word) {
        ExampleSentence blankExampleSentence = new ExampleSentence();
        blankExampleSentence.setEnglishSentence("");
        blankExampleSentence.setJapaneseSentence("");
        Collection<ExampleSentence> blankExamples = new HashSet<>();
        word.setExampleSentences(blankExamples);
    }
}
