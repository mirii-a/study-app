package com.japanese.study_app.repository;

import com.japanese.study_app.model.Word;
import com.japanese.study_app.model.WordDefinition;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WordDefinitionRepository extends JpaRepository<WordDefinition, Long>{
    WordDefinition findByWord(Word word);
}
