package com.japanese.study_app.service.word;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.testObjects.TestObjectsWord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordDtoServiceTest {

    private WordDtoService wordDtoService;

    private final TestObjectsWord helper = new TestObjectsWord();

    @BeforeEach
    void setUp() {
        this.wordDtoService = new WordDtoService();
    }

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
        assertTrue(result.exampleSentences().stream().anyMatch(sentence -> sentence.get("japaneseSentence").equals("国民に関する固定観念がどれほど有害かを理解するには、少数の外国人と親しくなるだけで十分だ。")));
        assertTrue(result.exampleSentences().stream().anyMatch(sentence -> sentence.get("japaneseSentence").equals("年下は年上に敬語を使うべきだというのが、あの世代の固定観念らしい。")));
        assertTrue(result.exampleSentences().stream().anyMatch(sentence -> sentence.get("japaneseSentence").equals("彼女は男性がより良いリーダーであるという固定観念を打破することを目指している。")));
        assertTrue(result.exampleSentences().stream().anyMatch(sentence -> sentence.get("englishSentence").equals("One only needs to become acquainted with a small number of foreigners to realise how harmful national stereotypes can be.")));
        assertTrue(result.exampleSentences().stream().anyMatch(sentence -> sentence.get("englishSentence").equals("It seems to be a stereotype of that generation that younger people should use honorific language when speaking to older people.")));
        assertTrue(result.exampleSentences().stream().anyMatch(sentence -> sentence.get("englishSentence").equals("She aims to break the stereotype that men are better leaders.")));
    }

}