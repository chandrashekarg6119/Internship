package com.example.codingplatform.controller;

import com.example.codingplatform.entity.Problem;
import com.example.codingplatform.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/problems")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    // ADD PROBLEM (ADMIN)
    @PostMapping
    public Problem addProblem(@RequestBody Problem problem) {
        return problemService.addProblem(problem);
    }

    // GET ALL PROBLEMS (USER)
    @GetMapping
    public List<Problem> getAllProblems() {
        return problemService.getAllProblems();
    }
}