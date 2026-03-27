package com.example.codingplatform.repository;

import com.example.codingplatform.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    // ✅ IMPORTANT: correct method name
    List<TestCase> findByProblem_Id(Long problemId);

}