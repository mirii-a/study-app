package com.japanese.study_app.controller;

import com.japanese.study_app.service.exampleSentence.IExampleSentenceService;
import com.japanese.study_app.service.word.IWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/examples")
public class ExampleSentenceController {

    private final IExampleSentenceService exampleSentenceService;
}
