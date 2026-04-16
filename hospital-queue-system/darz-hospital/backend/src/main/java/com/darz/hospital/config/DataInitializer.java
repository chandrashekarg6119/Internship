package com.darz.hospital.config;

import com.darz.hospital.entity.Doctor;
import com.darz.hospital.entity.Patient;
import com.darz.hospital.entity.User;
import com.darz.hospital.repository.DoctorRepository;
import com.darz.hospital.repository.PatientRepository;
import com.darz.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail("admin@darz.com")) {
            log.info("Demo data already exists — skipping seed.");
            return;
        }

        log.info("Seeding demo data...");

        // ── ADMIN ──────────────────────────────────────────────────
        userRepository.save(User.builder()
                .name("Admin User")
                .email("admin@darz.com")
                .password(passwordEncoder.encode("admin123"))
                .phone("9000000001")
                .role(User.Role.ADMIN)
                .build());

        // ── DOCTOR 1 ───────────────────────────────────────────────
        User doc1User = userRepository.save(User.builder()
                .name("Dr. Arjun Sharma")
                .email("doctor1@darz.com")
                .password(passwordEncoder.encode("doctor123"))
                .phone("9000000002")
                .role(User.Role.DOCTOR)
                .build());

        List<LocalDate> next7 = IntStream.range(0, 7)
                .mapToObj(i -> LocalDate.now().plusDays(i))
                .toList();

        doctorRepository.save(Doctor.builder()
                .user(doc1User)
                .specialization("Cardiologist")
                .qualification("MBBS, MD (Cardiology)")
                .experienceYears(12)
                .consultationFee(800.0)
                .bio("Senior cardiologist with 12+ years of experience in interventional cardiology.")
                .isAvailable(true)
                .availableDates(next7)
                .availableTimeStart("09:00")
                .availableTimeEnd("17:00")
                .build());

        // ── DOCTOR 2 ───────────────────────────────────────────────
        User doc2User = userRepository.save(User.builder()
                .name("Dr. Priya Nair")
                .email("doctor2@darz.com")
                .password(passwordEncoder.encode("doctor123"))
                .phone("9000000003")
                .role(User.Role.DOCTOR)
                .build());

        doctorRepository.save(Doctor.builder()
                .user(doc2User)
                .specialization("Dermatologist")
                .qualification("MBBS, MD (Dermatology)")
                .experienceYears(8)
                .consultationFee(600.0)
                .bio("Expert dermatologist specialising in skin disorders and cosmetic procedures.")
                .isAvailable(true)
                .availableDates(next7)
                .availableTimeStart("10:00")
                .availableTimeEnd("18:00")
                .build());

        // ── PATIENT 1 ──────────────────────────────────────────────
        User pat1User = userRepository.save(User.builder()
                .name("Rahul Verma")
                .email("patient1@darz.com")
                .password(passwordEncoder.encode("patient123"))
                .phone("9000000004")
                .role(User.Role.PATIENT)
                .build());

        patientRepository.save(Patient.builder()
                .user(pat1User)
                .gender("Male")
                .bloodGroup("O+")
                .address("123 MG Road, Bengaluru")
                .emergencyContact("9876543210")
                .build());

        // ── PATIENT 2 ──────────────────────────────────────────────
        User pat2User = userRepository.save(User.builder()
                .name("Sneha Patel")
                .email("patient2@darz.com")
                .password(passwordEncoder.encode("patient123"))
                .phone("9000000005")
                .role(User.Role.PATIENT)
                .build());

        patientRepository.save(Patient.builder()
                .user(pat2User)
                .gender("Female")
                .bloodGroup("A+")
                .address("456 Park Street, Mumbai")
                .emergencyContact("9876543211")
                .build());

        log.info("✅ Demo data seeded successfully!");
        log.info("   admin@darz.com    / admin123");
        log.info("   doctor1@darz.com  / doctor123");
        log.info("   doctor2@darz.com  / doctor123");
        log.info("   patient1@darz.com / patient123");
        log.info("   patient2@darz.com / patient123");
    }
}
