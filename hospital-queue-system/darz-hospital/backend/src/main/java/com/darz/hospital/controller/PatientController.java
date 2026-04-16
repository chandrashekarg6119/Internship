package com.darz.hospital.controller;

import com.darz.hospital.dto.PatientDTO;
import com.darz.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/admin/patients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PatientDTO.PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/patient/profile/{userId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    public ResponseEntity<PatientDTO.PatientResponse> getPatientByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(patientService.getPatientByUserId(userId));
    }

    @GetMapping("/admin/patients/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientDTO.PatientResponse> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    public ResponseEntity<PatientDTO.PatientResponse> updatePatient(
            @PathVariable Long patientId,
            @RequestBody PatientDTO.UpdatePatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, request));
    }

    @DeleteMapping("/admin/patients/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
