package com.japanese.study_app.repository;

import com.japanese.study_app.model.Word;
import com.japanese.study_app.model.WordDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WordDefinitionRepository extends JpaRepository<WordDefinition, Long>{
    List<WordDefinition> findByWord(Word word);
}
