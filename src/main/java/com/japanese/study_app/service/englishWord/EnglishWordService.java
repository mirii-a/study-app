package com.japanese.study_app.service.englishWord;

import com.japanese.study_app.exceptions.WordNotFoundException;
import com.japanese.study_app.model.EnglishWord;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.EnglishWordRepository;
import com.japanese.study_app.request.AddWordRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
public class EnglishWordService {

    private final EnglishWordRepository englishWordRepository;

    public EnglishWordService(EnglishWordRepository englishWordRepository) {
        this.englishWordRepository = englishWordRepository;
    }

    public Set<EnglishWord> dealWithEnglishWords(AddWordRequest request) {
        Set<EnglishWord> englishWords = request.englishWord();
        dealWithEnglishWordsInRepository(englishWords);
        return englishWords;
    }

    public Word updateEnglishWords(Word wordToUpdate, Set<EnglishWord> updatedEnglishWords) {
        checkIfEnglishWordsHaveBeenRemovedFromWord(wordToUpdate, updatedEnglishWords);
        dealWithEnglishWordsInRepository(updatedEnglishWords);
        wordToUpdate.setEnglishWord(updatedEnglishWords);
        // Ensure that the new English Word (if any) has the new word set
        addWordToEnglishWordTranslations(wordToUpdate);
        return wordToUpdate;
    }

    public void removeWordFromEnglishWord(EnglishWord englishWord, Word word) {
        removeWordMappingFromEnglishWords(englishWord, word);
    }

    public boolean checkIfWordExistsByEnglishWord(String englishWord) {
        return englishWordRepository.existsByEnglishWord(englishWord);
    }

    public EnglishWord findEnglishWordById(Long wordId) {
        return englishWordRepository.findById(wordId).orElseThrow(
                () -> new WordNotFoundException("English word with id " + wordId + " not found."));
    }

    public void addWordToEnglishWordTranslations(Word word) {
        Collection<EnglishWord> newEnglishWords = word.getEnglishWord().stream()
                .map(englishWord -> findEnglishWordById(englishWord.getId()))
                .toList();

        newEnglishWords.forEach(englishWord -> {
            Collection<Word> words = englishWord.getWord();
            if (!words.contains(word)) {
                words.add(word);
                englishWord.setWord(words);
                englishWordRepository.save(englishWord);
            }
        });
    }

    private void dealWithEnglishWordsInRepository(Set<EnglishWord> englishWords) {
        englishWords.forEach(englishWord -> {
            boolean englishWordExits = englishWordRepository.existsByEnglishWord(englishWord.getEnglishWord());
            if (!englishWordExits) {
                EnglishWord newEnglishWord = addEnglishWordToRepository(englishWord.getEnglishWord());
                englishWord.setId(newEnglishWord.getId());
                englishWord.setEnglishWord(newEnglishWord.getEnglishWord());
            } else {
                Optional<EnglishWord> existingEnglishWord =
                        englishWordRepository.findByEnglishWord(englishWord.getEnglishWord());
                existingEnglishWord.ifPresent(value -> {
                    // Updates the IDs of the English words if they already exist for the Word
                    englishWord.setId(value.getId());
//                    englishWord.setEnglishWord(value.getEnglishWord());
                });
            }
        });
    }

    private EnglishWord addEnglishWordToRepository(String englishWord) {
        EnglishWord newEnglishWord = new EnglishWord(englishWord);
        englishWordRepository.save(newEnglishWord);
        return newEnglishWord;
    }

    private void checkIfEnglishWordsHaveBeenRemovedFromWord(Word word, Set<EnglishWord> updatedEnglishWords) {
        for (EnglishWord englishWord : word.getEnglishWord()) {
            if (!updatedEnglishWords.contains(englishWord)) {
                // Remove mapping from database
                removeWordMappingFromEnglishWords(englishWord, word);
            }
        }
    }

    private void removeWordMappingFromEnglishWords(EnglishWord english, Word word) {
        Collection<Word> wordsForEnglishWord = english.getWord();
        wordsForEnglishWord.removeIf(matchingWord -> matchingWord.getEnglishWord().equals(word.getEnglishWord()));
        english.setWord(wordsForEnglishWord);
        englishWordRepository.save(english);
    }

}
