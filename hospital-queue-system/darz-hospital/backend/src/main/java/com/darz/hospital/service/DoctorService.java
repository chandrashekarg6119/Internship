package com.darz.hospital.service;

import com.darz.hospital.dto.DoctorDTO;
import com.darz.hospital.entity.Doctor;
import com.darz.hospital.entity.User;
import com.darz.hospital.repository.DoctorRepository;
import com.darz.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<DoctorDTO.DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorDTO.DoctorResponse> getAvailableDoctors() {
        return doctorRepository.findByIsAvailable(true).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DoctorDTO.DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return mapToResponse(doctor);
    }

    public DoctorDTO.DoctorResponse getDoctorByUserId(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        return mapToResponse(doctor);
    }

    public DoctorDTO.DoctorResponse updateAvailability(Long doctorId, DoctorDTO.UpdateAvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setAvailableDates(request.getAvailableDates());
        doctor.setAvailableTimeStart(request.getAvailableTimeStart());
        doctor.setAvailableTimeEnd(request.getAvailableTimeEnd());
        doctor.setIsAvailable(request.getIsAvailable());

        return mapToResponse(doctorRepository.save(doctor));
    }

    public DoctorDTO.DoctorResponse registerDoctor(DoctorDTO.RegisterDoctorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.DOCTOR)
                .build();
        user = userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .user(user)
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .consultationFee(request.getConsultationFee())
                .bio(request.getBio())
                .isAvailable(true)
                .build();

        return mapToResponse(doctorRepository.save(doctor));
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    private DoctorDTO.DoctorResponse mapToResponse(Doctor doctor) {
        return DoctorDTO.DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUser().getId())
                .name(doctor.getUser().getName())
                .email(doctor.getUser().getEmail())
                .phone(doctor.getUser().getPhone())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .consultationFee(doctor.getConsultationFee())
                .bio(doctor.getBio())
                .isAvailable(doctor.getIsAvailable())
                .availableDates(doctor.getAvailableDates())
                .availableTimeStart(doctor.getAvailableTimeStart())
                .availableTimeEnd(doctor.getAvailableTimeEnd())
                .build();
    }
}
