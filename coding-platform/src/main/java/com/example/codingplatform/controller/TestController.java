package com.example.codingplatform.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String test() {
        return "Project Working!";
    }
}