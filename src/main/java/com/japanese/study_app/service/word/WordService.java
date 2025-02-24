package com.japanese.study_app.service.word;

import java.util.*;
import java.util.stream.Collectors;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.model.EnglishWord;
import com.japanese.study_app.model.WordDefinition;
import com.japanese.study_app.repository.EnglishWordRepository;
import com.japanese.study_app.repository.WordDefinitionRepository;
import com.japanese.study_app.request.UpdateWordRequest;
import org.springframework.stereotype.Service;

import com.japanese.study_app.exceptions.AlreadyExistsException;
import com.japanese.study_app.exceptions.CategoryNotFoundException;
import com.japanese.study_app.exceptions.WordNotFoundException;
import com.japanese.study_app.model.Category;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.repository.CategoryRepository;
import com.japanese.study_app.repository.WordRepository;
import com.japanese.study_app.request.AddWordRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WordService implements IWordService {

    private final WordRepository wordRepository;
    private final CategoryRepository categoryRepository;
    private final EnglishWordRepository englishWordRepository;
    private final WordDefinitionRepository wordDefinitionRepository;
//    private final ModelMapper modelMapper;

    @Override
    public WordDto addWord(AddWordRequest request) {
        if (wordExists(request.getJapaneseWord())){
            throw new AlreadyExistsException(request.getJapaneseWord() + " already exists!");
        }

        Word newWord = wordRepository.save(createWord(request));

        addWordToEnglishWordMapping(newWord);

        addWordToCategoryMapping(newWord);

        Map<String, Set<String>> definitions = request.getDefinitions();
        dealWithDefinitions(newWord, definitions);

        return convertWordToDto(newWord);
    }

    private boolean wordExists(String japaneseWord){
        return wordRepository.existsByJapaneseWord(japaneseWord);
    }

    private Word createWord(AddWordRequest request){

        Set<EnglishWord> englishWords = dealWithEnglishWords(request);
        Set<Category> categories = dealWithCategories(request);

        return new Word(
            request.getJapaneseWord(),
            englishWords,
            request.getHiragana(),
            categories
        );
    }

    private Set<EnglishWord> dealWithEnglishWords(AddWordRequest request) {
        Set<EnglishWord> englishWords = request.getEnglishWord();
        dealWithEnglishWordsInRepository(englishWords);
        return englishWords;
    }

    private void dealWithEnglishWordsInRepository(Set<EnglishWord> englishWords){
        englishWords.forEach(englishWord-> {
            boolean englishWordExits = englishWordRepository.existsByEnglishWord(englishWord.getEnglishWord());
            if (!englishWordExits){
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

    private Set<Category> dealWithCategories(AddWordRequest request){
        Set<Category> categories = request.getCategory();
        dealWithCategoriesInRepository(categories);
        return categories;
    }

    private void dealWithCategoriesInRepository(Set<Category> categoryInput){
        categoryInput.forEach(category -> {
            boolean categoryExists = categoryRepository.existsByName(category.getName());
            if (!categoryExists){
                Category newCategory = new Category(category.getName());
                categoryRepository.save(newCategory);
                category.setId(newCategory.getId());
                category.setName(newCategory.getName());
            } else {
                Optional<Category> categoryFromDb = categoryRepository.findByName(category.getName());
                categoryFromDb.ifPresent(value -> {
                    category.setId(value.getId());
                    category.setName(value.getName());
                });
            }
        });
    }

    private void dealWithDefinitions(Word word, Map<String, Set<String>> definitions){
        // Updates the Word object so no need to return anything
        WordDefinition wordDefinition = new WordDefinition();

        if (definitions != null){
            // Get Japanese definitions and make them into one string
            if (wordDefinitionRepository.findByWord(word) != null){
                wordDefinition = wordDefinitionRepository.findByWord(word);
            }

            StringBuilder japaneseDefinitionStringBuilder = new StringBuilder();

            for (String japaneseDefinition : definitions.get("japanese")){
                japaneseDefinitionStringBuilder.append(japaneseDefinition).append(";");
            }

            String japaneseDefinitions = japaneseDefinitionStringBuilder.toString();
            wordDefinition.setDefinitionJapanese(japaneseDefinitions);

            // Get English definitions and make them into one string
            StringBuilder englishDefinitionStringBuilder = new StringBuilder();

            for (String englishDefinition : definitions.get("english")){
                englishDefinitionStringBuilder.append(englishDefinition).append(";");
            }

            String englishDefinitions = englishDefinitionStringBuilder.toString();
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
    
    @Override
    public WordDto getWordById(Long id) {
        return wordRepository.findById(id).map(this::convertWordToDto)
            .orElseThrow(() -> new WordNotFoundException("Word with given ID not found!"));
    }

    @Override
    public void deleteWordById(Long id) {
        Word word = wordRepository.findById(id).orElseThrow(() ->
                new WordNotFoundException("Word not found to delete!"));
        prepareToDeleteWordByDeletingForeginKeyData(word);
        wordRepository.findById(word.getId())
                .ifPresentOrElse(wordRepository::delete, () -> {throw new WordNotFoundException("Word not found to delete!");});
    }

    @Override
    public void deleteWordByJapaneseWord(String japaneseWord) {
        if (wordExists(japaneseWord)) {
            Word word = wordRepository.findByJapaneseWord(japaneseWord);
            prepareToDeleteWordByDeletingForeginKeyData(word);
            wordRepository.findById(word.getId())
                    .ifPresentOrElse(wordRepository::delete, () -> {throw new WordNotFoundException("Word not found to delete!");});
        } else {
            throw new WordNotFoundException("Word not found to delete!");
        }
    }

    private void prepareToDeleteWordByDeletingForeginKeyData(Word word){
        wordDefinitionRepository.delete(word.getDefinitions());

        for (Category category : word.getCategory()){
            Collection<Word> wordsForCategory = category.getWords();
            wordsForCategory.removeIf(wordInCategory -> wordInCategory.getJapaneseWord().equals(word.getJapaneseWord()));
            category.setWords(wordsForCategory);
            categoryRepository.save(category);
        }
        for (EnglishWord english : word.getEnglishWord()){
            Collection<Word> wordsForEnglishWord = english.getWord();
            wordsForEnglishWord.removeIf(matchingWord -> matchingWord.getEnglishWord().equals(word.getEnglishWord()));
            english.setWord(wordsForEnglishWord);
            englishWordRepository.save(english);
        }
    }

    @Override
    public WordDto updateWord(UpdateWordRequest request) {
        Word wordToUpdate = wordRepository.findByJapaneseWord(request.getJapaneseWord());
        if (wordToUpdate == null){
            throw new WordNotFoundException("No word '" + request.getJapaneseWord() + "' found to update.");
        }
        // update the database entries as needed
        Set<Category> updatedCategories = request.getCategory();
        dealWithCategoriesInRepository(updatedCategories);
        wordToUpdate.setCategory(updatedCategories);
        // Ensure that the new Category (if any) has the new word set
        addWordToCategoryMapping(wordToUpdate);

        Set<EnglishWord> updatedEnglishWords = request.getEnglishWord();
        dealWithEnglishWordsInRepository(updatedEnglishWords);
        wordToUpdate.setEnglishWord(updatedEnglishWords);
        // Ensure that the new English Word (if any) has the new word set
        addWordToEnglishWordMapping(wordToUpdate);

        wordToUpdate.setHiragana(request.getHiragana());
        wordToUpdate.setJapaneseWord(request.getJapaneseWord());

        wordRepository.save(wordToUpdate);

        Map<String, Set<String>> definitions = request.getDefinitions();
        dealWithDefinitions(wordToUpdate, definitions);

        return convertWordToDto(wordToUpdate);
    }

    @Override
    public WordDto getWordByJapaneseWord(String japaneseWord) {
        Word word = wordRepository.findByJapaneseWord(japaneseWord);
        return convertWordToDto(word);
    }

    @Override
    public List<WordDto> getWordsByEnglishWord(String englishWord) {
        List<Word> words =  wordRepository.findBySingleEnglishWord(englishWord);
        if (words.isEmpty()){
            // this should return 200 empty status?
            throw new WordNotFoundException("No words matching " + englishWord + " found. Please try again.");
        }
        return words.stream().map(this::convertWordToDto).collect(Collectors.toList());

    }

    @Override
    public List<WordDto> getWordsByHiragana(String hiragana) {
        List<Word> words =  wordRepository.findByHiragana(hiragana);
        if (words.isEmpty()){
            // this should return 200 empty status?
            throw new WordNotFoundException("No words matching " + hiragana + " found. Please try again.");
        }
        return words.stream().map(this::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public List<WordDto> getWordsByCategory(String category) {
        if (!categoryRepository.existsByName(category)){
            throw new CategoryNotFoundException("No category with name " + category + " found.");
        }

        List<Word> words =  wordRepository.findByCategoryName(category);
        if (words.isEmpty()){
            // this should return 200 empty status?
            throw new WordNotFoundException("No words with the category " + category + " have been found. Please add some words to this category and try again.");
        }
        return words.stream().map(this::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public List<WordDto> getWordsByHiraganaAndCategory(String hiragana, String category){
        // search if category exists
        if (!categoryRepository.existsByName(category)){
            throw new CategoryNotFoundException("No category with name " + category + " found.");
        }
        // search if hiragana exists
        List<Word> wordsWithHiragana = wordRepository.findByHiragana(hiragana);
        if (wordsWithHiragana.isEmpty()){
            throw new WordNotFoundException("No words with hiragana '" + hiragana + "' have been found.");
        }
        // combine searches
        List<Word> filteredWords = new ArrayList<>();
        wordsWithHiragana.forEach(word -> {
            Collection<Category> categories = word.getCategory();
            for (Category givenCategory : categories){
                if(givenCategory.getName().equals(category)){
                    filteredWords.add(word);
                }
            }
        });

        return filteredWords.stream().map(this::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public List<WordDto> getWordsByEnglishWordAndCategory(String englishWord, String category) {
        if (! categoryRepository.existsByName(category)){
            throw new CategoryNotFoundException("No category with name '" + category + "' found.");
        }
        if(! englishWordRepository.existsByEnglishWord(englishWord)){
            throw new WordNotFoundException("No English word '" + englishWord + "' found. Please try again.");
        }
        // Get words by English word
        List<Word> words = wordRepository.findBySingleEnglishWord(englishWord);
        if (words.isEmpty()){
            // this should return 200 empty status?
            throw new WordNotFoundException("No words matching '" + englishWord + "' have been found. Please try again.");
        }
        List<Word> filteredWords = new ArrayList<>();
        for (Word word : words){
            Collection<Category> categories = word.getCategory();
            for (Category givenCategory : categories){
                if(givenCategory.getName().contains(category)){
                    filteredWords.add(word);
                    break;
                }
            }
        }
        if (filteredWords.isEmpty()){
            // this should return 200 empty status?
            throw new WordNotFoundException("No words matching '" + englishWord + "' and category '" + category + "' have been found.");
        }
        return filteredWords.stream().map(this::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public List<WordDto> getAllWords() {
        List<Word> words = wordRepository.findAll();
        return words.stream().map(this::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public WordDto convertWordToDto(Word word){
        WordDto wordDto = new WordDto();
        wordDto.setId(word.getId());
        wordDto.setEnglishWord(word.getEnglishWord().stream().map(EnglishWord::getEnglishWord).collect(Collectors.toList()));
        wordDto.setJapaneseWord(word.getJapaneseWord());
        wordDto.setHiragana(word.getHiragana());
        wordDto.setCategories(word.getCategory().stream().map(Category::getName).collect(Collectors.toList()));

        if (word.getDefinitions() != null){
            WordDefinition definitionsForWord = word.getDefinitions();

            List<String> allEnglishDefinitions = new ArrayList<>();
            List<String> allJapaneseDefinitions = new ArrayList<>();


            String[] englishDefinitions = definitionsForWord.getDefinitionEnglish().split(";");
            String[] japaneseDefinitions = definitionsForWord.getDefinitionJapanese().split(";");

            allEnglishDefinitions.addAll(Arrays.stream(englishDefinitions).toList());
            allJapaneseDefinitions.addAll(Arrays.stream(japaneseDefinitions).toList());


            wordDto.setEnglishDefinitions(allEnglishDefinitions);
            wordDto.setJapaneseDefinitions(allJapaneseDefinitions);
        } else {
            List<String> blankList = new ArrayList<>();
            wordDto.setEnglishDefinitions(blankList);
            wordDto.setJapaneseDefinitions(blankList);
        }
        return wordDto;
    }

    private void addWordToEnglishWordMapping(Word word){
        Collection<EnglishWord> newEnglishWords = word.getEnglishWord().stream()
                .map(englishWord -> englishWordRepository.findById(englishWord.getId())
                        .orElseThrow(() -> new WordNotFoundException("English word with id " + englishWord.getId() + " not found.")))
                .toList();

        newEnglishWords.forEach(englishWord -> {
            Collection<Word> words = englishWord.getWord();
            if (! words.contains(word)){
                words.add(word);
                englishWord.setWord(words);
                englishWordRepository.save(englishWord);
            }
        });
    }

    private void addWordToCategoryMapping(Word word){
        List<Category> newCategories = word.getCategory().stream()
                .map(category -> categoryRepository.findById(category.getId())
                        .orElseThrow(() -> new CategoryNotFoundException("Category with id " + category.getId() + " not found.")))
                .toList();

        newCategories.forEach(category -> {
            Collection<Word> words = category.getWords();
            if (! words.contains(word)){
                words.add(word);
                category.setWords(words);
                categoryRepository.save(category);
            }
        });
    }

}
