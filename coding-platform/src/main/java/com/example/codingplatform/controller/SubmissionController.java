package com.example.codingplatform.controller;

import com.example.codingplatform.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping("/{problemId}")
    public String submit(@PathVariable Long problemId,
                         @RequestBody String code) {

        return submissionService.runCode(code, problemId);
    }
}