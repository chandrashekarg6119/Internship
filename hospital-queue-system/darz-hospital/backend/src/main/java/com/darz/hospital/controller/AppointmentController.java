package com.darz.hospital.controller;

import com.darz.hospital.dto.AppointmentDTO;
import com.darz.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/book/{userId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    public ResponseEntity<AppointmentDTO.AppointmentResponse> bookAppointment(
            @PathVariable Long userId,
            @RequestBody AppointmentDTO.BookRequest request) {
        return ResponseEntity.ok(appointmentService.bookAppointment(userId, request));
    }

    @PutMapping("/{appointmentId}/cancel/{userId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    public ResponseEntity<AppointmentDTO.AppointmentResponse> cancelAppointment(
            @PathVariable Long appointmentId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId, userId));
    }

    @GetMapping("/patient/{userId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    public ResponseEntity<List<AppointmentDTO.AppointmentResponse>> getPatientAppointments(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(userId));
    }

    @GetMapping("/doctor/{userId}")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public ResponseEntity<List<AppointmentDTO.AppointmentResponse>> getDoctorAppointments(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(userId));
    }

    @GetMapping("/queue/{doctorId}")
    public ResponseEntity<List<AppointmentDTO.AppointmentResponse>> getDoctorQueue(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getDoctorQueue(doctorId, date));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentDTO.AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PutMapping("/{appointmentId}/status")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public ResponseEntity<AppointmentDTO.AppointmentResponse> updateStatus(
            @PathVariable Long appointmentId,
            @RequestParam String status) {
        return ResponseEntity.ok(appointmentService.updateStatus(appointmentId, status));
    }
}
