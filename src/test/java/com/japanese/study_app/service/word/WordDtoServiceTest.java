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
        assertEquals(2, result.englishDefinitions().size());
        assertEquals(2, result.japaneseDefinitions().size());
        assertEquals(2, result.categories().size());

        assertTrue(result.englishDefinitions().stream().anyMatch(english -> english.equals("An idea that is always stuck in one's head and restricts one's thinking")));
        assertTrue(result.japaneseDefinitions().stream().anyMatch(japanese -> japanese.equals("いつも頭から離れないで、その人の思考を拘束するような考え")));
    }

}