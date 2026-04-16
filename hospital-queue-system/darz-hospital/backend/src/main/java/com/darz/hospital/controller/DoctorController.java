package com.darz.hospital.controller;

import com.darz.hospital.dto.DoctorDTO;
import com.darz.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/doctors/public/all")
    public ResponseEntity<List<DoctorDTO.DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/doctors/public/available")
    public ResponseEntity<List<DoctorDTO.DoctorResponse>> getAvailableDoctors() {
        return ResponseEntity.ok(doctorService.getAvailableDoctors());
    }

    @GetMapping("/doctors/public/{id}")
    public ResponseEntity<DoctorDTO.DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping("/doctor/profile/{userId}")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public ResponseEntity<DoctorDTO.DoctorResponse> getDoctorByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(doctorService.getDoctorByUserId(userId));
    }

    @PutMapping("/doctor/{doctorId}/availability")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public ResponseEntity<DoctorDTO.DoctorResponse> updateAvailability(
            @PathVariable Long doctorId,
            @RequestBody DoctorDTO.UpdateAvailabilityRequest request) {
        return ResponseEntity.ok(doctorService.updateAvailability(doctorId, request));
    }

    @PostMapping("/admin/doctors/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDTO.DoctorResponse> registerDoctor(@RequestBody DoctorDTO.RegisterDoctorRequest request) {
        return ResponseEntity.ok(doctorService.registerDoctor(request));
    }

    @DeleteMapping("/admin/doctors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
