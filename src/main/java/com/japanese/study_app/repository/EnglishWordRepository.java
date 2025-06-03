package com.japanese.study_app.repository;

import com.japanese.study_app.model.EnglishWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnglishWordRepository extends JpaRepository<EnglishWord, Long> {
    Optional<EnglishWord> findByEnglishWord(String englishWord);

    boolean existsByEnglishWord(String name);
}
