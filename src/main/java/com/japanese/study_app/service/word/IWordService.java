package com.japanese.study_app.service.word;

import java.util.List;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.request.AddWordRequest;
import com.japanese.study_app.request.UpdateWordRequest;

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

    WordDto convertWordToDto(Word word);

}
