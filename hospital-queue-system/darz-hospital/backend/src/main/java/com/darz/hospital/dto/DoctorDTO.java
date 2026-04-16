package com.darz.hospital.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

public class DoctorDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DoctorResponse {
        private Long id;
        private Long userId;
        private String name;
        private String email;
        private String phone;
        private String specialization;
        private String qualification;
        private Integer experienceYears;
        private Double consultationFee;
        private String bio;
        private Boolean isAvailable;
        private List<LocalDate> availableDates;
        private String availableTimeStart;
        private String availableTimeEnd;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAvailabilityRequest {
        private List<LocalDate> availableDates;
        private String availableTimeStart;
        private String availableTimeEnd;
        private Boolean isAvailable;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterDoctorRequest {
        private String name;
        private String email;
        private String password;
        private String phone;
        private String specialization;
        private String qualification;
        private Integer experienceYears;
        private Double consultationFee;
        private String bio;
    }
}
