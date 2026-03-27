package com.example.codingplatform.service;

import com.example.codingplatform.entity.TestCase;
import com.example.codingplatform.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private TestCaseRepository testCaseRepository;

    public String runCode(String userCode, Long problemId) {

        try {

            // ✅ FIXED METHOD NAME
            List<TestCase> testCases =
                    testCaseRepository.findByProblem_Id(problemId);

            for (TestCase tc : testCases) {

                // TEMP LOGIC (just print)
                System.out.println("Input: " + tc.getInput());
                System.out.println("Expected: " + tc.getExpectedOutput());
            }

            return "PASSED";

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}