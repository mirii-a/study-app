package com.japanese.study_app.controller;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.exceptions.AlreadyExistsException;
import com.japanese.study_app.exceptions.WordNotFoundException;
import com.japanese.study_app.request.AddWordRequest;
import com.japanese.study_app.request.UpdateWordRequest;
import com.japanese.study_app.response.ApiResponse;
import com.japanese.study_app.service.word.IWordService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/words")
public class WordController {

    private final IWordService wordService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllWords() {
        try {
            List<WordDto> allWords = wordService.getAllWords();
            return ResponseEntity.ok(new ApiResponse("All words retrieved successfully.", allWords));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to find words.", INTERNAL_SERVER_ERROR));
        }

    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWord(@RequestBody AddWordRequest word) {
        try{
            WordDto newWord = wordService.addWord(word);
            return ResponseEntity.ok(new ApiResponse("Word added successfully.", newWord));
        } catch(AlreadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<ApiResponse> deleteWordById(@PathVariable Long id) {
        try{
            wordService.deleteWordById(id);
            return ResponseEntity.ok(new ApiResponse("Word deleted successfully.", null));
        } catch(WordNotFoundException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/word/{kanji}")
    public ResponseEntity<ApiResponse> deleteWordByJapaneseWord(@PathVariable String kanji) {
        try{
            wordService.deleteWordByJapaneseWord(kanji);
            return ResponseEntity.ok(new ApiResponse("Word deleted successfully.", null));
        } catch(WordNotFoundException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/hiragana/{hiragana}")
    public ResponseEntity<ApiResponse> getWordsByHiragana(@PathVariable String hiragana) {
        try {
            List<WordDto> words = wordService.getWordsByHiragana(hiragana);
            return ResponseEntity.ok(new ApiResponse("Words with hiragana '" + hiragana + "' retrieved successfully.", words));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to find words matching given criteria.", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/kanji/{kanji}")
    public ResponseEntity<ApiResponse> getWordByKanji(@PathVariable String kanji) {
        try {
            WordDto word = wordService.getWordByJapaneseWord(kanji);
            return ResponseEntity.ok(new ApiResponse("Word with kanji '" + kanji + "' retrieved successfully.", word));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to find words matching given criteria.", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/english/{english}")
    public ResponseEntity<ApiResponse> getWordsByEnglishWord(@PathVariable String english) {
        try {
            List<WordDto> word = wordService.getWordsByEnglishWord(english);
            return ResponseEntity.ok(new ApiResponse("Words with English translation '" + english + "' retrieved successfully.", word));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to find words matching given criteria.", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse> getWordsByCategory(@PathVariable String category) {
        try {
            List<WordDto> word = wordService.getWordsByCategory(category);
            return ResponseEntity.ok(new ApiResponse("Words with category '" + category + "' retrieved successfully.", word));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to find words matching given criteria.", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/english/{english}/category/{category}")
    public ResponseEntity<ApiResponse> getWordsByEnglishWordAndCategory(@PathVariable String english, @PathVariable String category) {
        try {
            List<WordDto> words = wordService.getWordsByEnglishWordAndCategory(english, category);
            return ResponseEntity.ok(new ApiResponse("Words matching English '" + english + " and category '" + category + "' retrieved successfully.", words));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to find words matching given criteria.", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/hiragana/{hiragana}/category/{category}")
    public ResponseEntity<ApiResponse> getWordsByHiraganaAndCategory(@PathVariable String hiragana, @PathVariable String category) {
        try {
            List<WordDto> words = wordService.getWordsByHiraganaAndCategory(hiragana, category);
            return ResponseEntity.ok(new ApiResponse("Words matching hiragana '" + hiragana + " and category '" + category + "' retrieved successfully.", words));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to find words matching given criteria.", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse> getWordsById(@PathVariable Long id) {
        try {
            WordDto word = wordService.getWordById(id);
            return ResponseEntity.ok(new ApiResponse("Word with id " + id + " retrieved successfully.", word));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() + " Failed to find words matching given criteria.", INTERNAL_SERVER_ERROR));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateWord(@RequestBody UpdateWordRequest updateRequest){
        try {
            WordDto updatedWord = wordService.updateWord(updateRequest);
            return ResponseEntity.ok(new ApiResponse("Word '" + updateRequest.getJapaneseWord() + "' successfully updated.", updatedWord));
        } catch (WordNotFoundException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
