package com.example.hospitalqueuesystem.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.example.hospitalqueuesystem.entity.Doctor;
import com.example.hospitalqueuesystem.repository.DoctorRepository;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    DoctorRepository doctorRepository;

    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor doctor){
        return doctorRepository.save(doctor);
    }

    @GetMapping
    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

}