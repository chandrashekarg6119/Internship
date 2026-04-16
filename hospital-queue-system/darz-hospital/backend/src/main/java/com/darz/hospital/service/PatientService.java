package com.darz.hospital.service;

import com.darz.hospital.dto.PatientDTO;
import com.darz.hospital.entity.Patient;
import com.darz.hospital.repository.PatientRepository;
import com.darz.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public List<PatientDTO.PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PatientDTO.PatientResponse getPatientByUserId(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));
        return mapToResponse(patient);
    }

    public PatientDTO.PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return mapToResponse(patient);
    }

    public PatientDTO.PatientResponse updatePatient(Long patientId, PatientDTO.UpdatePatientRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContact(request.getEmergencyContact());
        patient.setMedicalHistory(request.getMedicalHistory());

        return mapToResponse(patientRepository.save(patient));
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    private PatientDTO.PatientResponse mapToResponse(Patient patient) {
        return PatientDTO.PatientResponse.builder()
                .id(patient.getId())
                .userId(patient.getUser().getId())
                .name(patient.getUser().getName())
                .email(patient.getUser().getEmail())
                .phone(patient.getUser().getPhone())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .address(patient.getAddress())
                .emergencyContact(patient.getEmergencyContact())
                .medicalHistory(patient.getMedicalHistory())
                .build();
    }
}
