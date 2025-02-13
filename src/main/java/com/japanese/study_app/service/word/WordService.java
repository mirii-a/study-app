package com.japanese.study_app.service.word;

import java.util.*;
import java.util.stream.Collectors;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.model.EnglishWord;
import com.japanese.study_app.repository.EnglishWordRepository;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    public WordDto addWord(AddWordRequest request) {
        if (wordExists(request.getJapaneseWord())){
            throw new AlreadyExistsException(request.getJapaneseWord() + " already exists!");
        }

        Word newWord = wordRepository.save(createWord(request));

        Collection<EnglishWord> newEnglishWords = newWord.getEnglishWord().stream()
                .map(englishWord -> englishWordRepository.findById(englishWord.getId())
                        .orElseThrow(() -> new WordNotFoundException("English word with id " + englishWord.getId() + " not found.")))
                .toList();

        newEnglishWords.forEach(englishWord -> {
            Collection<Word> words = englishWord.getWord();
            words.add(newWord);
            englishWord.setWord(words);
            englishWordRepository.save(englishWord);
        });

        List<Category> newCategories = newWord.getCategory().stream()
                .map(category -> categoryRepository.findById(category.getId())
                        .orElseThrow(() -> new CategoryNotFoundException("Category with id " + category.getId() + " not found.")))
                .toList();

        newCategories.forEach(category -> {
            Collection<Word> words = category.getWords();
            words.add(newWord);
            category.setWords(words);
            categoryRepository.save(category);
        });

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
        return englishWords;
    }

    private Set<Category> dealWithCategories(AddWordRequest request){
        Set<Category> categories = request.getCategory();
        categories.forEach(category -> {
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
        return categories;
    }
    
    @Override
    public Word getWordById(Long id) {
        return wordRepository.findById(id)
            .orElseThrow(() -> new WordNotFoundException("Word not found!"));
    }

    @Override
    public void deleteWordById(Long id) {
        // wordRepository::delete is a method reference that refers to the delete method of the wordRepository
        wordRepository.findById(id)
            .ifPresentOrElse(wordRepository::delete, () -> {throw new WordNotFoundException("Word not found to delete!");});
    }

    @Override
    public void deleteWordByJapaneseWord(String japaneseWord) {
        // wordRepository::delete is a method reference that refers to the delete method of the wordRepository
//        wordRepository.findByJapaneseWord(japaneseWord)
//                .ifPresentOrElse(wordRepository::delete, () -> {throw new WordNotFoundException("Word not found to delete!");});
    }

    @Override
    public Word updateWord() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateWord'");
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

//    @Override
//    public List<Word> getWordsByEnglishWordAndCategory(String englishWord, String category) {
//        if (! categoryRepository.existsByName(category)){
//            throw new CategoryNotFoundException("No category with name " + category + " found.");
//        }
//        if(! wordRepository.existsByEnglishWord(englishWord)){
//            throw new WordNotFoundException("No word with English " + englishWord + " found.");
//        }
//
//        List<Word> words = wordRepository.findByEnglishWord(englishWord);
//        if (words.isEmpty()){
//            // this should return 200 empty status?
//            throw new WordNotFoundException("No words with the English " + englishWord + " have been found. Please try again.");
//        }
//        List<Word> filteredWords = new ArrayList<>();
//        for (Word word : words){
//            Collection<Category> categories = word.getCategory();
//            for (Category givenCategory : categories){
//                if(givenCategory.getName().contains(category)){
//                    filteredWords.add(word);
//                    break;
//                }
//            }
//        }
//        if (filteredWords.isEmpty()){
//            // this should return 200 empty status?
//            throw new WordNotFoundException("No words with the English " + englishWord + " and category " + category + " have been found. Please add some words to this category and try again.");
//        }
//        return filteredWords;
//    }

//    @Override
//    public List<Word> getWordsByJapaneseWordAndCategory(String japaneseWord, String category) {
//        if (! categoryRepository.existsByName(category)){
//            throw new CategoryNotFoundException("No category with name " + category + " found.");
//        }
//        if(! wordRepository.existsByJapaneseWord(japaneseWord)){
//            throw new WordNotFoundException("No word with Japanese " + japaneseWord + " found.");
//        }
//
//        List<Word> words = wordRepository.findByJapaneseWordAndCategoryName(japaneseWord, category);
//        if (words.isEmpty()){
//            // this should return 200 empty status?
//            throw new WordNotFoundException("No words with the Japanese " + japaneseWord + " and category " + category + " have been found. Please add some words to this category and try again.");
//        }
//        return words;
//    }

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
        return wordDto;
    }
}
