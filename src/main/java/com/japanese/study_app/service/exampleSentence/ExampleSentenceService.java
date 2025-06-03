package com.japanese.study_app.service.exampleSentence;

import com.japanese.study_app.exceptions.ExampleSentenceNotFoundException;
import com.japanese.study_app.exceptions.MissingExampleSentenceException;
import com.japanese.study_app.model.ExampleSentence;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.ExampleSentenceRepository;
import com.japanese.study_app.request.RequestWordExampleSentences;
import org.springframework.stereotype.Service;

import java.util.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Service
public class ExampleSentenceService implements IExampleSentenceService {

    private static final Logger log = LogManager.getLogger(ExampleSentenceService.class);

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

    public void dealWithExampleSentences(Word word, Set<RequestWordExampleSentences> newExampleSentences) {
        if (newExampleSentences != null) {
            newExampleSentences.forEach(newExample -> {
                if (newExample.japaneseSentence() == null || newExample.englishSentence() == null) {
                    throw new MissingExampleSentenceException("Missing either Japanese translation or English "
                            + "translation for example sentence. Please review and resubmit.");
                }
                if (exampleSentenceRepository.existsByJapaneseSentence(newExample.japaneseSentence())
                        && exampleSentenceRepository.existsByEnglishSentence(newExample.englishSentence())) {
                    log.info("Both Japanese and English example sentences already exist in the repository. Skipping...");
                }
                else {
                    if (!Objects.equals(newExample.japaneseSentence(), "")) {
                        if (exampleSentenceRepository.existsByJapaneseSentence(newExample.japaneseSentence()) && !exampleSentenceRepository.existsByEnglishSentence(newExample.englishSentence())) {
                            updateEnglishExampleSentenceAsJapaneseSentenceHasNotChanged(word, newExample);
                        } else if (exampleSentenceRepository.existsByEnglishSentence(newExample.englishSentence()) && !exampleSentenceRepository.existsByJapaneseSentence(newExample.japaneseSentence())) {
                            updateJapaneseExampleSentenceAsEnglishSentenceHasNotChanged(word, newExample);
                        } else {
                            addNewExampleSentenceToRepo(word, newExample);
                            Collection<ExampleSentence> exampleSentencesForWord = exampleSentenceRepository.findByWords(word);
                            word.setExampleSentences(exampleSentencesForWord);
                        }
                    } else {
                        // Set as blank list as there are no definitions
                        setBlankExampleSentence(word);
                    }
                }
            });
        } else {
            // Set as blank list as there are no definitions
            setBlankExampleSentence(word);
        }
    }

    private void addNewExampleSentenceToRepo(Word word, RequestWordExampleSentences newExample) {
        log.info("Adding new example sentence to repository...");
        ExampleSentence sentenceExample = new ExampleSentence();
        sentenceExample.setEnglishSentence(newExample.englishSentence());
        sentenceExample.setJapaneseSentence(newExample.japaneseSentence());
        Collection<Word> words = new HashSet<>();
        // Add word to mapping for this new example sentence
        words.add(word);
        sentenceExample.setWords(words);
        // save new example to repository
        exampleSentenceRepository.save(sentenceExample);
    }

    private void setBlankExampleSentence(Word word) {
        ExampleSentence blankExampleSentence = new ExampleSentence();
        blankExampleSentence.setEnglishSentence("");
        blankExampleSentence.setJapaneseSentence("");
        Collection<ExampleSentence> blankExamples = new HashSet<>();
        word.setExampleSentences(blankExamples);
    }

    private void updateEnglishExampleSentenceAsJapaneseSentenceHasNotChanged(Word word, RequestWordExampleSentences newExampleSentence) {
        ExampleSentence sentenceExample = new ExampleSentence();
        Optional<ExampleSentence> sentenceAlreadyExisting = exampleSentenceRepository.findByJapaneseSentence(newExampleSentence.japaneseSentence());
        sentenceAlreadyExisting.ifPresent(existingSentence -> {
            if (englishSentenceHasBeenUpdated(
                    existingSentence.getEnglishSentence(), newExampleSentence.englishSentence())) {
                sentenceExample.setJapaneseSentence(existingSentence.getJapaneseSentence());
                sentenceExample.setEnglishSentence(newExampleSentence.englishSentence());
                sentenceExample.setId(existingSentence.getId());
            } else {
                sentenceExample.setJapaneseSentence(existingSentence.getJapaneseSentence());
                sentenceExample.setEnglishSentence(existingSentence.getEnglishSentence());
                sentenceExample.setId(existingSentence.getId());
            }
            Collection<Word> words = existingSentence.getWords();

            if (!newExampleSentence.englishSentence().equals(existingSentence.getEnglishSentence())) {
                existingSentence.setEnglishSentence(newExampleSentence.englishSentence());
                if (!words.contains(word)) {
                    words.add(word);
                    existingSentence.setWords(words);
                }
                exampleSentenceRepository.save(existingSentence);
            }

            Collection<ExampleSentence> exampleSentencesForWord = exampleSentenceRepository.findByWords(word);
            word.setExampleSentences(exampleSentencesForWord);

        });
    }

    private void updateJapaneseExampleSentenceAsEnglishSentenceHasNotChanged(Word word, RequestWordExampleSentences newExampleSentence) {
        ExampleSentence sentenceExample = new ExampleSentence();
        Optional<ExampleSentence> sentenceAlreadyExisting = exampleSentenceRepository.findByEnglishSentence(newExampleSentence.englishSentence());
        sentenceAlreadyExisting.ifPresent(existingSentence -> {
            if (japaneseSentenceHasBeenUpdated(
                    existingSentence.getJapaneseSentence(), newExampleSentence.japaneseSentence())) {
                sentenceExample.setJapaneseSentence(newExampleSentence.japaneseSentence());
                sentenceExample.setEnglishSentence(existingSentence.getEnglishSentence());
                sentenceExample.setId(existingSentence.getId());
            } else {
                sentenceExample.setJapaneseSentence(existingSentence.getJapaneseSentence());
                sentenceExample.setEnglishSentence(existingSentence.getEnglishSentence());
                sentenceExample.setId(existingSentence.getId());
            }
            Collection<Word> words = existingSentence.getWords();

            if (!newExampleSentence.japaneseSentence().equals(existingSentence.getJapaneseSentence())) {
                existingSentence.setJapaneseSentence(newExampleSentence.japaneseSentence());
                if (!words.contains(word)) {
                    words.add(word);
                    existingSentence.setWords(words);
                }
                exampleSentenceRepository.save(existingSentence);
            }
            Collection<ExampleSentence> exampleSentencesForWord = exampleSentenceRepository.findByWords(word);
            word.setExampleSentences(exampleSentencesForWord);
        });
    }

    private boolean englishSentenceHasBeenUpdated(String existingSentence, String englishSentenceInRequest) {
        return !existingSentence.equals(englishSentenceInRequest);
    }

    private boolean japaneseSentenceHasBeenUpdated(String existingSentence, String japaneseSentenceInRequest) {
        return !existingSentence.equals(japaneseSentenceInRequest);
    }
}
