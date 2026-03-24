package com.example.hospitalqueuesystem.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.example.hospitalqueuesystem.entity.Appointment;
import com.example.hospitalqueuesystem.service.AppointmentService;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // ✅ FIXED: pass userId + doctorId
    @PostMapping("/book")
    public Appointment book(
            @RequestParam Long userId,
            @RequestParam Long doctorId,
            @RequestBody Appointment appointment) {

        return appointmentService.bookAppointment(userId, doctorId, appointment);
    }

    // ✅ FIXED method name
    @GetMapping
    public List<Appointment> all() {
        return appointmentService.getAllAppointments();
    }
}