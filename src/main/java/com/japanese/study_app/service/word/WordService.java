package com.japanese.study_app.service.word;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.exceptions.AlreadyExistsException;
import com.japanese.study_app.exceptions.CategoryNotFoundException;
import com.japanese.study_app.exceptions.WordNotFoundException;
import com.japanese.study_app.model.Category;
import com.japanese.study_app.model.EnglishWord;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.WordRepository;
import com.japanese.study_app.request.AddWordRequest;
import com.japanese.study_app.request.RequestWordExampleSentences;
import com.japanese.study_app.request.UpdateWordRequest;
import com.japanese.study_app.service.category.CategoryService;
import com.japanese.study_app.service.englishWord.EnglishWordService;
import com.japanese.study_app.service.exampleSentence.ExampleSentenceService;
import com.japanese.study_app.service.wordDefinitions.WordDefinitionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class WordService implements IWordService {

    private final WordRepository wordRepository;
    private final CategoryService categoryService;
    private final EnglishWordService englishWordService;
    private final WordDefinitionService wordDefinitionService;
    private final ExampleSentenceService exampleSentenceService;
    private final WordDtoService wordDtoService;

    public WordService(WordRepository wordRepository,
                       CategoryService categoryService,
                       EnglishWordService englishWordService,
                       WordDefinitionService wordDefinitionService,
                       ExampleSentenceService exampleSentenceService,
                       WordDtoService wordDtoService
    ) {
        this.wordRepository = wordRepository;
        this.categoryService = categoryService;
        this.englishWordService = englishWordService;
        this.wordDefinitionService = wordDefinitionService;
        this.exampleSentenceService = exampleSentenceService;
        this.wordDtoService = wordDtoService;

    }

    @Override
    public WordDto addWord(AddWordRequest request) {
        if (wordExists(request.japaneseWord())) {
            throw new AlreadyExistsException(request.japaneseWord() + " already exists!");
        }

        Word newWord = wordRepository.save(createWord(request));

        englishWordService.addWordToEnglishWordTranslations(newWord);
        categoryService.addWordToCategoryMapping(newWord);

        wordDefinitionService.dealWithDefinitions(newWord, request.definitions());

        exampleSentenceService.dealWithExampleSentences(newWord, request.exampleSentence());

        return wordDtoService.convertWordToDto(newWord);
    }

    private boolean wordExists(String japaneseWord) {
        return wordRepository.existsByJapaneseWord(japaneseWord);
    }

    private Word createWord(AddWordRequest request) {
        Set<EnglishWord> englishWords = englishWordService.dealWithEnglishWords(request);
        Set<Category> categories = categoryService.dealWithCategories(request.category());

        return new Word(
                request.japaneseWord(),
                englishWords,
                request.hiragana(),
                categories
        );
    }

    @Override
    public WordDto getWordById(Long id) {
        return wordRepository.findById(id).map(wordDtoService::convertWordToDto)
                .orElseThrow(() -> new WordNotFoundException("Word with given ID not found!"));
    }

    @Override
    public void deleteWordById(Long id) {
        Word word = wordRepository.findById(id).orElseThrow(() ->
                new WordNotFoundException("Word not found to delete!"));
        prepareToDeleteWordByDeletingForeignKeyData(word);
        wordRepository.findById(word.getId())
                .ifPresentOrElse(wordRepository::delete, () -> {
                    throw new WordNotFoundException("Word not found to delete!");
                });
    }

    @Override
    public void deleteWordByJapaneseWord(String japaneseWord) {
        if (wordExists(japaneseWord)) {
            Word word = wordRepository.findByJapaneseWord(japaneseWord);
            prepareToDeleteWordByDeletingForeignKeyData(word);
            wordRepository.findById(word.getId())
                    .ifPresentOrElse(wordRepository::delete, () -> {
                        throw new WordNotFoundException("Word not found to delete!");
                    });
        } else {
            throw new WordNotFoundException("Word not found to delete!");
        }
    }

    private void prepareToDeleteWordByDeletingForeignKeyData(Word word) {

        wordDefinitionService.removeDefinitionsForWord(word);

        exampleSentenceService.deleteExampleSentences(word);

        word.getCategory().forEach(
                category ->
                        categoryService.removeWordFromCategory(category, word));

        word.getEnglishWord().forEach(
                englishWord ->
                        englishWordService.removeWordFromEnglishWord(englishWord, word));
    }

    @Override
    public WordDto updateWord(UpdateWordRequest request) {
        Word wordToUpdate = wordRepository.findByJapaneseWord(request.japaneseWord());
        if (wordToUpdate == null) {
            throw new WordNotFoundException("No word '" + request.japaneseWord() + "' found to update.");
        }
        // update the database entries as needed

        wordToUpdate = categoryService.updateCategories(wordToUpdate, request.category());
        wordToUpdate = englishWordService.updateEnglishWords(wordToUpdate, request.englishWord());

        wordToUpdate.setHiragana(request.hiragana());
        wordToUpdate.setJapaneseWord(request.japaneseWord());

        wordRepository.save(wordToUpdate);

        wordDefinitionService.dealWithDefinitions(wordToUpdate, request.definitions());

        Set<RequestWordExampleSentences> exampleSentences = request.exampleSentence();
        exampleSentenceService.dealWithExampleSentences(wordToUpdate, exampleSentences);

        return wordDtoService.convertWordToDto(wordToUpdate);
    }

    @Override
    public WordDto getWordByJapaneseWord(String japaneseWord) {
        Word word = wordRepository.findByJapaneseWord(japaneseWord);
        return wordDtoService.convertWordToDto(word);
    }

    @Override
    public List<WordDto> getWordsByEnglishWord(String englishWord) {
        List<Word> words = wordRepository.findBySingleEnglishWord(englishWord);
        if (words.isEmpty()) {
            // this should return 200 empty status?
            throw new WordNotFoundException("No words matching " + englishWord + " found. Please try again.");
        }
        return words.stream().map(wordDtoService::convertWordToDto).collect(Collectors.toList());

    }

    @Override
    public List<WordDto> getWordsByHiragana(String hiragana) {
        List<Word> words = wordRepository.findByHiragana(hiragana);
        if (words.isEmpty()) {
            // this should return 200 empty status?
            throw new WordNotFoundException("No words matching " + hiragana + " found. Please try again.");
        }
        return words.stream().map(wordDtoService::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public List<WordDto> getWordsByCategory(String category) {
        checkIfCategoryExists(category);

        List<Word> words = wordRepository.findByCategoryName(category);
        if (words.isEmpty()) {
            // this should return 200 empty status?
            throw new WordNotFoundException("No words with the category " + category + " have been found. Please add some words to this category and try again.");
        }
        return words.stream().map(wordDtoService::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public List<WordDto> getWordsByHiraganaAndCategory(String hiragana, String category) {
        // search if category exists
        checkIfCategoryExists(category);
        // search if hiragana exists
        List<Word> wordsWithHiragana = wordRepository.findByHiragana(hiragana);
        if (wordsWithHiragana.isEmpty()) {
            throw new WordNotFoundException("No words with hiragana '" + hiragana + "' have been found.");
        }
        // combine searches
        List<Word> filteredWords = new ArrayList<>();
        wordsWithHiragana.forEach(word -> {
            Collection<Category> categories = word.getCategory();
            for (Category givenCategory : categories) {
                if (givenCategory.getName().equals(category)) {
                    filteredWords.add(word);
                }
            }
        });

        return filteredWords.stream().map(wordDtoService::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public List<WordDto> getWordsByEnglishWordAndCategory(String englishWord, String category) {
        checkIfCategoryExists(category);

        if (!englishWordService.checkIfWordExistsByEnglishWord(englishWord)) {
            throw new WordNotFoundException("No English word '" + englishWord + "' found. Please try again.");
        }
        // Get words by English word
        List<Word> words = wordRepository.findBySingleEnglishWord(englishWord);
        if (words.isEmpty()) {
            // this should return 200 empty status?
            throw new WordNotFoundException("No words matching '" + englishWord + "' have been found. Please try again.");
        }
        List<Word> filteredWords = new ArrayList<>();
        for (Word word : words) {
            Collection<Category> categories = word.getCategory();
            for (Category givenCategory : categories) {
                if (givenCategory.getName().contains(category)) {
                    filteredWords.add(word);
                    break;
                }
            }
        }
        if (filteredWords.isEmpty()) {
            // this should return 200 empty status?
            throw new WordNotFoundException("No words matching '" + englishWord + "' and category '" + category + "' have been found.");
        }
        return filteredWords.stream().map(wordDtoService::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public List<WordDto> getAllWords() {
        List<Word> words = wordRepository.findAll();
        return words.stream().map(wordDtoService::convertWordToDto).collect(Collectors.toList());
    }

    private void checkIfCategoryExists(String category) {
        if (!categoryService.checkIfCategoryExistsByName(category)) {
            throw new CategoryNotFoundException("No category with name '" + category + "' found.");
        }
    }

}
