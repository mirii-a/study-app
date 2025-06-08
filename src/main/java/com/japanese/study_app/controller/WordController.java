package com.japanese.study_app.controller;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.request.AddWordRequest;
import com.japanese.study_app.request.UpdateWordRequest;
import com.japanese.study_app.response.ApiResponse;
import com.japanese.study_app.service.word.IWordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/words")
public class WordController {

    private final IWordService wordService;

    public WordController(IWordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllWords() {
        List<WordDto> allWords = wordService.getAllWords();
        return ResponseEntity.ok(new ApiResponse("All words retrieved successfully.", allWords));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWord(@RequestBody AddWordRequest word) {
        WordDto newWord = wordService.addWord(word);
        return ResponseEntity.ok(new ApiResponse("Word added successfully.", newWord));
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<ApiResponse> deleteWordById(@PathVariable Long id) {
        wordService.deleteWordById(id);
        return ResponseEntity.ok(new ApiResponse("Word deleted successfully.", null));
    }

    @DeleteMapping("/delete/word/{kanji}")
    public ResponseEntity<ApiResponse> deleteWordByJapaneseWord(@PathVariable String kanji) {
        wordService.deleteWordByJapaneseWord(kanji);
        return ResponseEntity.ok(new ApiResponse("Word deleted successfully.", null));
    }

    @GetMapping("/hiragana/{hiragana}")
    public ResponseEntity<ApiResponse> getWordsByHiragana(@PathVariable String hiragana) {
        List<WordDto> words = wordService.getWordsByHiragana(hiragana);
        return ResponseEntity.ok(new ApiResponse("Words with hiragana '" + hiragana
                + "' retrieved successfully.", words));
    }

    @GetMapping("/kanji/{kanji}")
    public ResponseEntity<ApiResponse> getWordByKanji(@PathVariable String kanji) {
        WordDto word = wordService.getWordByJapaneseWord(kanji);
        return ResponseEntity.ok(new ApiResponse("Word with kanji '" + kanji
                + "' retrieved successfully.", word));
    }

    @GetMapping("/english/{english}")
    public ResponseEntity<ApiResponse> getWordsByEnglishWord(@PathVariable String english) {
        List<WordDto> word = wordService.getWordsByEnglishWord(english);
        return ResponseEntity.ok(new ApiResponse("Words with English translation '" + english
                + "' retrieved successfully.", word));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse> getWordsByCategory(@PathVariable String category) {
        List<WordDto> word = wordService.getWordsByCategory(category);
        return ResponseEntity.ok(new ApiResponse("Words with category '" + category
                + "' retrieved successfully.", word));
    }

    @GetMapping("/english/{english}/category/{category}")
    public ResponseEntity<ApiResponse> getWordsByEnglishWordAndCategory(@PathVariable String english,
                                                                        @PathVariable String category) {
        List<WordDto> words = wordService.getWordsByEnglishWordAndCategory(english, category);
        return ResponseEntity.ok(new ApiResponse("Words matching English '" + english
                + " and category '" + category + "' retrieved successfully.", words));
    }

    @GetMapping("/hiragana/{hiragana}/category/{category}")
    public ResponseEntity<ApiResponse> getWordsByHiraganaAndCategory(@PathVariable String hiragana,
                                                                     @PathVariable String category) {
        List<WordDto> words = wordService.getWordsByHiraganaAndCategory(hiragana, category);
        return ResponseEntity.ok(new ApiResponse("Words matching hiragana '" + hiragana
                + " and category '" + category + "' retrieved successfully.", words));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse> getWordsById(@PathVariable Long id) {
        WordDto word = wordService.getWordById(id);
        return ResponseEntity.ok(new ApiResponse("Word with id " + id
                + " retrieved successfully.", word));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateWord(@RequestBody UpdateWordRequest updateRequest) {
        WordDto updatedWord = wordService.updateWord(updateRequest);
        return ResponseEntity.ok(new ApiResponse("Word '" + updateRequest.japaneseWord()
                + "' successfully updated.", updatedWord));
    }

}
