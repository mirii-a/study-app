package com.japanese.study_app.controller;

import com.japanese.study_app.service.study.IStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/study")
public class StudyController {

    private final IStudyService studyService;
}
