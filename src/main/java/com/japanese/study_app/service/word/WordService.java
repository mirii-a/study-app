package com.japanese.study_app.service.word;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.exceptions.AlreadyExistsException;
import com.japanese.study_app.exceptions.CategoryNotFoundException;
import com.japanese.study_app.exceptions.WordNotFoundException;
import com.japanese.study_app.model.*;
import com.japanese.study_app.repository.ExampleSentenceRepository;
import com.japanese.study_app.repository.WordDefinitionRepository;
import com.japanese.study_app.repository.WordRepository;
import com.japanese.study_app.request.AddWordRequest;
import com.japanese.study_app.request.UpdateWordRequest;
import com.japanese.study_app.service.category.CategoryService;
import com.japanese.study_app.service.englishWord.EnglishWordService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class WordService implements IWordService {

    private final WordRepository wordRepository;
    private final CategoryService categoryService;
    private final EnglishWordService englishWordService;
    private final WordDefinitionRepository wordDefinitionRepository;
    private final ExampleSentenceRepository exampleSentenceRepository;

    public WordService(WordRepository wordRepository,
                       CategoryService categoryService,
                       EnglishWordService englishWordService,
                       WordDefinitionRepository wordDefinitionRepository,
                       ExampleSentenceRepository exampleSentenceRepository
    ) {
        this.wordRepository = wordRepository;
        this.categoryService = categoryService;
        this.englishWordService = englishWordService;
        this.wordDefinitionRepository = wordDefinitionRepository;
        this.exampleSentenceRepository = exampleSentenceRepository;

    }

    @Override
    public WordDto addWord(AddWordRequest request) {
        if (wordExists(request.japaneseWord())) {
            throw new AlreadyExistsException(request.japaneseWord() + " already exists!");
        }

        Word newWord = wordRepository.save(createWord(request));

        englishWordService.addWordToEnglishWordTranslations(newWord);
        categoryService.addWordToCategoryMapping(newWord);

        Map<String, Set<String>> definitions = request.definitions();
        dealWithDefinitions(newWord, definitions);

        Set<Map<String, String>> exampleSentences = request.exampleSentence();
        dealWithExampleSentences(newWord, exampleSentences);

        return convertWordToDto(newWord);
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

    private void dealWithExampleSentences(Word word, Set<Map<String, String>> exampleSentences) {
        // TODO: Add better validation when wanting to update a word. Separate out this logic and move to ExampleSentenceService
        if (exampleSentences != null) {
            exampleSentences.forEach(example -> {
                if (!Objects.equals(example.get("japaneseSentence"), "") && example.get("japaneseSentence") != null) {
                    if (exampleSentenceRepository.existsByJapaneseSentence(example.get("japaneseSentence"))) {
                        ExampleSentence sentenceExample = new ExampleSentence();
                        Optional<ExampleSentence> sentenceAlreadyExisting = exampleSentenceRepository.findByJapaneseSentence(example.get("japaneseSentence"));
                        sentenceAlreadyExisting.ifPresent(value -> {
                            if (!value.getEnglishSentence().equals(example.get("englishSentence"))) {
                                sentenceExample.setJapaneseSentence(value.getJapaneseSentence());
                                sentenceExample.setEnglishSentence(example.get("englishSentence"));
                                sentenceExample.setId(value.getId());
                            } else {
                                sentenceExample.setJapaneseSentence(value.getJapaneseSentence());
                                sentenceExample.setEnglishSentence(value.getEnglishSentence());
                                sentenceExample.setId(value.getId());
                            }
                            Collection<Word> words = value.getWords();

                            if (!example.get("englishSentence").equals(value.getEnglishSentence())) {
                                value.setEnglishSentence(example.get("englishSentence"));
                                if (!words.contains(word)) {
                                    words.add(word);
                                    value.setWords(words);
                                }
                                exampleSentenceRepository.save(value);
                            }

                            Collection<ExampleSentence> exampleSentencesForWord = exampleSentenceRepository.findByWords(word);
                            word.setExampleSentences(exampleSentencesForWord);
                        });
                    } else if (exampleSentenceRepository.existsByEnglishSentence(example.get("englishSentence")) && !exampleSentenceRepository.existsByJapaneseSentence(example.get("japaneseSentence"))) {
                        ExampleSentence sentenceExample = new ExampleSentence();
                        Optional<ExampleSentence> sentenceAlreadyExisting = exampleSentenceRepository.findByEnglishSentence(example.get("englishSentence"));
                        sentenceAlreadyExisting.ifPresent(value -> {
                            if (!value.getJapaneseSentence().equals(example.get("japaneseSentence"))) {
                                sentenceExample.setJapaneseSentence(example.get("japaneseSentence"));
                                sentenceExample.setEnglishSentence(value.getEnglishSentence());
                                sentenceExample.setId(value.getId());
                            } else {
                                sentenceExample.setJapaneseSentence(value.getJapaneseSentence());
                                sentenceExample.setEnglishSentence(value.getEnglishSentence());
                                sentenceExample.setId(value.getId());
                            }
                            Collection<Word> words = value.getWords();

                            if (!example.get("japaneseSentence").equals(value.getJapaneseSentence())) {
                                value.setJapaneseSentence(example.get("japaneseSentence"));
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
                        sentenceExample.setEnglishSentence(example.get("englishSentence"));
                        sentenceExample.setJapaneseSentence(example.get("japaneseSentence"));
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

    private void dealWithDefinitions(Word word, Map<String, Set<String>> definitions) {
        // Updates the Word object so no need to return anything
        WordDefinition wordDefinition = new WordDefinition();

        if (definitions != null) {
            // Get Japanese definitions and make them into one string
            if (wordDefinitionRepository.findByWord(word) != null) {
                wordDefinition = wordDefinitionRepository.findByWord(word);
            }

            StringBuilder japaneseDefinitionStringBuilder = new StringBuilder();

            for (String japaneseDefinition : definitions.get("japanese")) {
                japaneseDefinitionStringBuilder.append(japaneseDefinition).append(";");
            }

            String japaneseDefinitions = japaneseDefinitionStringBuilder.toString();
            wordDefinition.setDefinitionJapanese(japaneseDefinitions);

            // Get English definitions and make them into one string
            StringBuilder englishDefinitionStringBuilder = new StringBuilder();

            for (String englishDefinition : definitions.get("english")) {
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
        if (word.getDefinitions() != null) {
            wordDefinitionRepository.delete(word.getDefinitions());
        }

        if (word.getExampleSentences() != null) {
            for (ExampleSentence sentence : word.getExampleSentences()) {
                exampleSentenceRepository.delete(sentence);
            }
        }

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

        Map<String, Set<String>> definitions = request.definitions();
        dealWithDefinitions(wordToUpdate, definitions);

        Set<Map<String, String>> exampleSentences = request.exampleSentence();
        dealWithExampleSentences(wordToUpdate, exampleSentences);

        return convertWordToDto(wordToUpdate);
    }

    @Override
    public WordDto getWordByJapaneseWord(String japaneseWord) {
        Word word = wordRepository.findByJapaneseWord(japaneseWord);
        return convertWordToDto(word);
    }

    @Override
    public List<WordDto> getWordsByEnglishWord(String englishWord) {
        List<Word> words = wordRepository.findBySingleEnglishWord(englishWord);
        if (words.isEmpty()) {
            // this should return 200 empty status?
            throw new WordNotFoundException("No words matching " + englishWord + " found. Please try again.");
        }
        return words.stream().map(this::convertWordToDto).collect(Collectors.toList());

    }

    @Override
    public List<WordDto> getWordsByHiragana(String hiragana) {
        List<Word> words = wordRepository.findByHiragana(hiragana);
        if (words.isEmpty()) {
            // this should return 200 empty status?
            throw new WordNotFoundException("No words matching " + hiragana + " found. Please try again.");
        }
        return words.stream().map(this::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public List<WordDto> getWordsByCategory(String category) {
        checkIfCategoryExists(category);

        List<Word> words = wordRepository.findByCategoryName(category);
        if (words.isEmpty()) {
            // this should return 200 empty status?
            throw new WordNotFoundException("No words with the category " + category + " have been found. Please add some words to this category and try again.");
        }
        return words.stream().map(this::convertWordToDto).collect(Collectors.toList());
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

        return filteredWords.stream().map(this::convertWordToDto).collect(Collectors.toList());
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
        return filteredWords.stream().map(this::convertWordToDto).collect(Collectors.toList());
    }

    @Override
    public List<WordDto> getAllWords() {
        List<Word> words = wordRepository.findAll();
        return words.stream().map(this::convertWordToDto).collect(Collectors.toList());
    }

    @Override
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

    private WordDto setDefinitionsForWordDto(WordDto wordDto, List<String> englishDefinitions, List<String> japaneseDefinitions) {
        wordDto = wordDto.updateEnglishDefinitions(englishDefinitions);
        wordDto = wordDto.updateJapaneseDefinitions(japaneseDefinitions);
        return wordDto;
    }

    private List<String> convertDefinitionsToListOfStringsForDtoOutput(String[] definitions) {
        List<String> listOfDefinitions = new ArrayList<>();
        listOfDefinitions.addAll(Arrays.stream(definitions).toList());
        return listOfDefinitions;
    }

    private void checkIfCategoryExists(String category) {
        if (!categoryService.checkIfCategoryExistsByName(category)) {
            throw new CategoryNotFoundException("No category with name '" + category + "' found.");
        }
    }

}
