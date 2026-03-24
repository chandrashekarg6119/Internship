package com.example.hospitalqueuesystem.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.example.hospitalqueuesystem.entity.Doctor;
import com.example.hospitalqueuesystem.entity.Appointment;
import com.example.hospitalqueuesystem.repository.DoctorRepository;
import com.example.hospitalqueuesystem.repository.AppointmentRepository;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // ✅ Add Doctor
    @PostMapping("/doctor")
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // ✅ Get All Doctors
    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // ✅ Update Doctor
    @PutMapping("/doctor/{id}")
    public Doctor updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {

        Doctor doctor = doctorRepository.findById(id).orElseThrow();

        doctor.setName(updatedDoctor.getName());
        doctor.setSpecialization(updatedDoctor.getSpecialization());
        doctor.setAvailability(updatedDoctor.getAvailability());

        return doctorRepository.save(doctor);
    }

    // ✅ Delete Doctor
    @DeleteMapping("/doctor/{id}")
    public String deleteDoctor(@PathVariable Long id) {

        doctorRepository.deleteById(id);

        return "Doctor deleted successfully";
    }

    // ✅ View All Appointments
    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

}