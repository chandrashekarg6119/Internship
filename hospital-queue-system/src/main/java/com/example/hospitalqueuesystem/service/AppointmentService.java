package com.example.hospitalqueuesystem.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.example.hospitalqueuesystem.entity.*;
import com.example.hospitalqueuesystem.repository.*;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Book Appointment with Queue Logic
    public Appointment bookAppointment(Long userId, Long doctorId, Appointment appointment) {

        User patient = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Queue Logic
        int queueNumber = appointmentRepository
                .findByDoctorId(doctorId)
                .size() + 1;

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setQueueNumber(queueNumber);
        appointment.setStatus("BOOKED");

        return appointmentRepository.save(appointment);
    }

    // ✅ Get All Appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // ✅ Get Appointments by Doctor
    public List<Appointment> getByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // ✅ Cancel Appointment
    public void cancelAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }

}