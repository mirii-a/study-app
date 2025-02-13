package com.japanese.study_app.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.japanese.study_app.model.Word;
import org.springframework.data.jpa.repository.Query;

public interface WordRepository extends JpaRepository<Word, Long>{
    List<Word> findByCategoryName(String category);
    @Query("select w from Word w join EnglishWord e on element(e.word).id = w.id where e.englishWord = ?1")
    List<Word> findBySingleEnglishWord(String englishWord);

//    List<Word> findByJapaneseWordAndEnglishWord(String japaneseWord, List<String> englishWord);
    Word findByJapaneseWord(String japaneseWord);
    List<Word> findByHiragana(String hiragana);
    // List<Word> findByExampleSentences(String exampleSentence);
    // List<Word> findByDefinitions(String definition);
//    List<Word> findByEnglishWordAndCategoryName(List<String> englishWord, String category);
//    List<Word> findByJapaneseWordAndCategoryName(String japaneseWord, String category);

    // Long countByBrandAndName(String brand, String name);
//    boolean existsByJapaneseWordAndEnglishWord(String japaneseWord, List<String> englishWord);
//    boolean existsByEnglishWord(String englishWord);
    boolean existsByJapaneseWord(String japaneseWord);
}
