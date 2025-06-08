package com.japanese.study_app.controller;

import com.japanese.study_app.model.ExampleSentence;
import com.japanese.study_app.response.ApiResponse;
import com.japanese.study_app.service.exampleSentence.IExampleSentenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/examples")
public class ExampleSentenceController {

    private final IExampleSentenceService exampleSentenceService;

    public ExampleSentenceController(IExampleSentenceService exampleSentenceService) {
        this.exampleSentenceService = exampleSentenceService;
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<ApiResponse> deleteExampleById(@PathVariable Long id) {
        exampleSentenceService.deleteExampleSentenceById(id);
        return ResponseEntity.ok(new ApiResponse("Example Sentence deleted successfully.", null));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllExampleSentences() {
        List<ExampleSentence> examples = exampleSentenceService.getAllExampleSentences();
        return ResponseEntity.ok(new ApiResponse("All example sentences retrieved successfully.", examples));
    }
}
