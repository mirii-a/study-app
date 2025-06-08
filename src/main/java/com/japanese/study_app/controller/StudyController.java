package com.japanese.study_app.controller;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.response.ApiResponse;
import com.japanese.study_app.service.study.IStudyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RestController
@RequestMapping("${api.prefix}/study")
public class StudyController {

    private final IStudyService studyService;

    public StudyController(IStudyService studyService) {
        this.studyService = studyService;
    }

    @GetMapping("/random")
    public ResponseEntity<ApiResponse> getRandomWords() {
        List<WordDto> randomWords = studyService.getRandomWords();
        return ResponseEntity.ok(new ApiResponse("Random words retrieved successfully.", randomWords));
    }

    @GetMapping("/random/{number}")
    public ResponseEntity<ApiResponse> getNumberOfRandomWords(@PathVariable Long number) {
        try {
            List<WordDto> randomWords = studyService.getNumberOfRandomWords(number);
            return ResponseEntity.ok(new ApiResponse("Random words retrieved successfully.", randomWords));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Retrieval of random words failed.", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/random/category/{category}")
    public ResponseEntity<ApiResponse> getRandomWordsByCategory(@PathVariable String category) {
        List<WordDto> randomWords = studyService.getRandomWordsByCategory(category);
        return ResponseEntity.ok(new ApiResponse("Random words retrieved successfully.", randomWords));
    }

    @GetMapping("/random/category/{category}/number/{number}")
    public ResponseEntity<ApiResponse> getNumberOfRandomWordsByCategory(@PathVariable String category, @PathVariable Long number) {
        List<WordDto> randomWords = studyService.getNumberOfRandomWordsByCategory(category, number);
        return ResponseEntity.ok(new ApiResponse("Random words retrieved successfully.", randomWords));
    }

    @GetMapping("/random/english/{english}")
    public ResponseEntity<ApiResponse> getRandomWordsByEnglishWord(@PathVariable String english) {
        List<WordDto> randomWords = studyService.getRandomWordsByEnglishWord(english);
        return ResponseEntity.ok(new ApiResponse("Random words retrieved successfully.", randomWords));
    }

    @GetMapping("/random/english/{english}/number/{number}")
    public ResponseEntity<ApiResponse> getNumberOfRandomWordsByEnglishWord(@PathVariable String english, @PathVariable Long number) {
        List<WordDto> randomWords = studyService.getNumberOfRandomWordsByEnglishWord(english, number);
        return ResponseEntity.ok(new ApiResponse("Random words retrieved successfully.", randomWords));
    }
}
