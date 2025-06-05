package com.japanese.study_app.service.word;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.request.AddWordRequest;
import com.japanese.study_app.request.UpdateWordRequest;

import java.util.List;

public interface IWordService {

    WordDto addWord(AddWordRequest request);

    WordDto getWordById(Long id);

    void deleteWordById(Long id);

    void deleteWordByJapaneseWord(String japaneseWord);

    WordDto updateWord(UpdateWordRequest request);
    // Collection<ExampleSentence> getExampleSentencesByWordId(Long wordId);
    // Collection<WordDefinition> getDefinitionByWordId(Long wordId);

    // Collection<Category> getWordCategoriesById(Long wordId);
    List<WordDto> getAllWords();

    WordDto getWordByJapaneseWord(String japaneseWord);

    List<WordDto> getWordsByHiragana(String hiragana);

    List<WordDto> getWordsByCategory(String category);

    List<WordDto> getWordsByEnglishWord(String englishWord);

    List<WordDto> getWordsByEnglishWordAndCategory(String englishWord, String category);

    List<WordDto> getWordsByHiraganaAndCategory(String hiragana, String category);

}
