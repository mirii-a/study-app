package com.japanese.study_app.repository;

import com.japanese.study_app.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyRepository extends JpaRepository<Word, Long> {
    @Query("select w from Word w order by RAND() limit 3")
    List<Word> findRandomWords();

    @Query("select w from Word w order by RAND() limit ?1")
    List<Word> findNumberOfRandomWords(Long number);
}
