package com.japanese.study_app.controller;

import com.japanese.study_app.exceptions.ExampleSentenceNotFoundException;
import com.japanese.study_app.model.ExampleSentence;
import com.japanese.study_app.response.ApiResponse;
import com.japanese.study_app.service.exampleSentence.IExampleSentenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RestController
@RequestMapping("${api.prefix}/examples")
public class ExampleSentenceController {

    private final IExampleSentenceService exampleSentenceService;

    public ExampleSentenceController(IExampleSentenceService exampleSentenceService) {
        this.exampleSentenceService = exampleSentenceService;
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<ApiResponse> deleteExampleById(@PathVariable Long id) {
        try {
            exampleSentenceService.deleteExampleSentenceById(id);
            return ResponseEntity.ok(new ApiResponse("Example Sentence deleted successfully.", null));
        } catch (ExampleSentenceNotFoundException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllExampleSentences() {
        try {
            List<ExampleSentence> examples = exampleSentenceService.getAllExampleSentences();
            return ResponseEntity.ok(new ApiResponse("All example sentences retrieved successfully.", examples));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to find words.", INTERNAL_SERVER_ERROR));
        }
    }
}
