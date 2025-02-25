package com.japanese.study_app.repository;

import com.japanese.study_app.model.ExampleSentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExampleSentenceRepository extends JpaRepository<ExampleSentence, Long> {
    boolean existsByJapaneseSentence(String japaneseSentence);
    Optional<ExampleSentence> findByJapaneseSentence(String japaneseSentence);
}
