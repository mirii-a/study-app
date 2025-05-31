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

    public boolean checkIfWordExistsByEnglishWord(String englishWord) {
        return englishWordRepository.existsByEnglishWord(englishWord);
    }

    public EnglishWord findEnglishWordById(Long wordId) {
        return englishWordRepository.findById(wordId).orElseThrow(() -> new WordNotFoundException("English word with id " + wordId + " not found."));
    }

    public void addWordToEnglishWordMapping(Word word) {
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

    public void checkIfEnglishWordsHaveBeenRemovedFromWord(Word word, Set<EnglishWord> updatedEnglishWords) {
        for (EnglishWord englishWord : word.getEnglishWord()) {
            if (!updatedEnglishWords.contains(englishWord)) {
                // Remove mapping from database
                removeWordFromEnglishWordTranslation(englishWord, word);
            }
        }
    }

    public void removeWordFromEnglishWordTranslation(EnglishWord english, Word word) {
        Collection<Word> wordsForEnglishWord = english.getWord();
        wordsForEnglishWord.removeIf(matchingWord -> matchingWord.getEnglishWord().equals(word.getEnglishWord()));
        english.setWord(wordsForEnglishWord);
        englishWordRepository.save(english);
    }

    public void dealWithEnglishWordsInRepository(Set<EnglishWord> englishWords) {
        englishWords.forEach(englishWord -> {
            boolean englishWordExits = englishWordRepository.existsByEnglishWord(englishWord.getEnglishWord());
            if (!englishWordExits) {
                EnglishWord newEnglishWord = new EnglishWord(englishWord.getEnglishWord());
                englishWordRepository.save(newEnglishWord);
                englishWord.setId(newEnglishWord.getId());
                englishWord.setEnglishWord(newEnglishWord.getEnglishWord());
            } else {
                Optional<EnglishWord> englishFromDb = englishWordRepository.findByEnglishWord(englishWord.getEnglishWord());
                englishFromDb.ifPresent(value -> {
                    englishWord.setId(value.getId());
                    englishWord.setEnglishWord(value.getEnglishWord());
                });
            }
        });
    }

}
