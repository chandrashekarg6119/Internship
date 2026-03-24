package com.example.hospitalqueuesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hospitalqueuesystem.entity.Appointment;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long>{

    List<Appointment> findByDoctorId(Long doctorId);

}