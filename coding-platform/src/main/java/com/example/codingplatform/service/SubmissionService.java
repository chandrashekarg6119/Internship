package com.example.codingplatform.service;

import com.example.codingplatform.entity.TestCase;
import com.example.codingplatform.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private TestCaseRepository testCaseRepository;

    public String runCode(String userCode, Long problemId) {

        try {
            // Save code
            File file = new File("Main.java");
            FileWriter writer = new FileWriter(file);
            writer.write(userCode);
            writer.close();

            // Compile
            Process compile = Runtime.getRuntime().exec("javac Main.java");
            compile.waitFor();

            // Get test cases
            List<TestCase> testCases = testCaseRepository.findByProblemId(problemId);

            for (TestCase tc : testCases) {

                Process run = Runtime.getRuntime().exec("java Main");

                BufferedWriter input = new BufferedWriter(
                        new OutputStreamWriter(run.getOutputStream()));

                input.write(tc.getInput());
                input.newLine();
                input.flush();
                input.close();

                BufferedReader output = new BufferedReader(
                        new InputStreamReader(run.getInputStream()));

                String result = output.readLine();

                if (!result.trim().equals(tc.getExpectedOutput().trim())) {
                    return "FAILED";
                }
            }

            return "PASSED";

        } catch (Exception e) {
            return "ERROR";
        }
    }
}