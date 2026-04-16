package com.darz.hospital.dto;

import lombok.*;
import java.time.LocalDate;

public class PatientDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatientResponse {
        private Long id;
        private Long userId;
        private String name;
        private String email;
        private String phone;
        private LocalDate dateOfBirth;
        private String gender;
        private String bloodGroup;
        private String address;
        private String emergencyContact;
        private String medicalHistory;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePatientRequest {
        private LocalDate dateOfBirth;
        private String gender;
        private String bloodGroup;
        private String address;
        private String emergencyContact;
        private String medicalHistory;
    }
}
