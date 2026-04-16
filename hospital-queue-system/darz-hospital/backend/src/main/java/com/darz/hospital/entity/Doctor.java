package com.darz.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private Double consultationFee;
    private String bio;
    private String profileImage;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @ElementCollection
    @CollectionTable(name = "doctor_availability", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "available_date")
    private List<LocalDate> availableDates;

    @Column(name = "available_time_start")
    private String availableTimeStart;

    @Column(name = "available_time_end")
    private String availableTimeEnd;
}
