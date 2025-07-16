package com.japanese.study_app.service.word;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.testObjects.TestObjectsWord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WordDtoServiceTest {

    private WordDtoService wordDtoService;

    private final TestObjectsWord helper = new TestObjectsWord();

    @BeforeEach
    void setUp() { this.wordDtoService = new WordDtoService();}

    @Test
    void convertWordToDto() {
        Word stereotype = helper.getKoteiKanNen();
        WordDto result = wordDtoService.convertWordToDto(stereotype);

        assertThat(result.getClass()).isEqualTo(WordDto.class);
        assertEquals(result.japaneseWord(), stereotype.getJapaneseWord());
    }

}