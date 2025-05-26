package com.japanese.study_app.repository;

import com.japanese.study_app.model.ExampleSentence;
import com.japanese.study_app.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface ExampleSentenceRepository extends JpaRepository<ExampleSentence, Long> {
    boolean existsByJapaneseSentence(String japaneseSentence);
    boolean existsByEnglishSentence(String englishSentence);
    boolean existsById(Long Id);
    Optional<ExampleSentence> findByJapaneseSentence(String japaneseSentence);
    Optional<ExampleSentence> findByEnglishSentence(String englishSentence);
    Collection<ExampleSentence> findByWords(Word word);
}
