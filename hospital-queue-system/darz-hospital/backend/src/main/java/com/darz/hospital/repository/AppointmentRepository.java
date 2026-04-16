package com.darz.hospital.repository;

import com.darz.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);
    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(Long patientId);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status != 'CANCELLED'")
    Integer countByDoctorIdAndDate(Long doctorId, LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS') ORDER BY a.queueNumber")
    List<Appointment> findQueueByDoctorAndDate(Long doctorId, LocalDate date);
}
