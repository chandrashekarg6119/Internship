package com.example.hospitalqueuesystem.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.example.hospitalqueuesystem.entity.Doctor;
import com.example.hospitalqueuesystem.repository.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // ✅ Add Doctor
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // ✅ Get All Doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // ✅ Update Doctor
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setName(updatedDoctor.getName());
        doctor.setSpecialization(updatedDoctor.getSpecialization());
        doctor.setAvailability(updatedDoctor.getAvailability());

        return doctorRepository.save(doctor);
    }

    // ✅ Delete Doctor
    public void deleteDoctor(Long id) {

        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found");
        }

        doctorRepository.deleteById(id);
    }
}