package com.darz.hospital.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookRequest {
        private Long doctorId;
        private LocalDate appointmentDate;
        private LocalTime appointmentTime;
        private String symptoms;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AppointmentResponse {
        private Long id;
        private Long patientId;
        private String patientName;
        private Long doctorId;
        private String doctorName;
        private String doctorSpecialization;
        private LocalDate appointmentDate;
        private LocalTime appointmentTime;
        private String status;
        private String symptoms;
        private String notes;
        private Integer queueNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueueUpdate {
        private Long appointmentId;
        private Integer queueNumber;
        private String status;
    }
}
