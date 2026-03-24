package com.example.hospitalqueuesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hospitalqueuesystem.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor,Long>{

}