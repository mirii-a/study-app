package com.japanese.study_app.repository;

import com.japanese.study_app.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findByCategoryName(String category);

    @Query("select w from Word w join EnglishWord e on element(e.word).id = w.id where e.englishWord = ?1")
    List<Word> findBySingleEnglishWord(String englishWord);

    //    List<Word> findByJapaneseWordAndEnglishWord(String japaneseWord, List<String> englishWord);
    Word findByJapaneseWord(String japaneseWord);

    List<Word> findByHiragana(String hiragana);
    // List<Word> findByExampleSentences(String exampleSentence);
    // List<Word> findByDefinitions(String definition);

    //    boolean existsByEnglishWord(String englishWord);
    boolean existsByJapaneseWord(String japaneseWord);
}
