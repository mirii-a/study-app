package com.japanese.study_app.controller;

import com.japanese.study_app.dto.WordDto;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.response.ApiResponse;
import com.japanese.study_app.service.study.IStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/study")
public class StudyController {

    private final IStudyService studyService;

    @GetMapping("/random")
    public ResponseEntity<ApiResponse> getRandomWords(){
        try {
            List<WordDto> randomWords = studyService.getRandomWordSet();
            return ResponseEntity.ok(new ApiResponse("Random words retrieved successfully.", randomWords));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to find words.", INTERNAL_SERVER_ERROR));
        }
    }
}
