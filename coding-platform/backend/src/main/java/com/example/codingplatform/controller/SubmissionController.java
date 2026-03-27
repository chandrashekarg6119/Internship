package com.example.codingplatform.controller;

import com.example.codingplatform.entity.TestCase;
import com.example.codingplatform.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/submit")
public class SubmissionController {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @PostMapping("/{problemId}")
    public String submit(@PathVariable Long problemId,
                         @RequestBody String code) {

        System.out.println("Received code:");
        System.out.println(code);

        // Fetch test cases using correct method
        List<TestCase> testCases =
                testCaseRepository.findByProblem_Id(problemId);

        if (testCases.isEmpty()) {
            return "No test cases found";
        }

        // 🔥 TEMP LOGIC (for now)
        // Later you can execute code here

        return "PASSED";
    }
}