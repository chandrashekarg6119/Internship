package com.darz.hospital.service;

import com.darz.hospital.dto.AppointmentDTO;
import com.darz.hospital.entity.Appointment;
import com.darz.hospital.entity.Doctor;
import com.darz.hospital.entity.Patient;
import com.darz.hospital.repository.AppointmentRepository;
import com.darz.hospital.repository.DoctorRepository;
import com.darz.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public AppointmentDTO.AppointmentResponse bookAppointment(Long userId, AppointmentDTO.BookRequest request) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Integer queueCount = appointmentRepository.countByDoctorIdAndDate(doctor.getId(), request.getAppointmentDate());
        int queueNumber = (queueCount == null ? 0 : queueCount) + 1;

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .symptoms(request.getSymptoms())
                .status(Appointment.AppointmentStatus.CONFIRMED)
                .queueNumber(queueNumber)
                .build();

        appointment = appointmentRepository.save(appointment);
        notifyQueueUpdate(doctor.getId(), request.getAppointmentDate());

        return mapToResponse(appointment);
    }

    public AppointmentDTO.AppointmentResponse cancelAppointment(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment = appointmentRepository.save(appointment);

        notifyQueueUpdate(appointment.getDoctor().getId(), appointment.getAppointmentDate());
        return mapToResponse(appointment);
    }

    public List<AppointmentDTO.AppointmentResponse> getPatientAppointments(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patient.getId())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<AppointmentDTO.AppointmentResponse> getDoctorAppointments(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return appointmentRepository.findByDoctorId(doctor.getId())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<AppointmentDTO.AppointmentResponse> getDoctorQueue(Long doctorId, LocalDate date) {
        return appointmentRepository.findQueueByDoctorAndDate(doctorId, date)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<AppointmentDTO.AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public AppointmentDTO.AppointmentResponse updateStatus(Long appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(Appointment.AppointmentStatus.valueOf(status.toUpperCase()));
        appointment = appointmentRepository.save(appointment);
        notifyQueueUpdate(appointment.getDoctor().getId(), appointment.getAppointmentDate());
        return mapToResponse(appointment);
    }

    private void notifyQueueUpdate(Long doctorId, LocalDate date) {
        List<AppointmentDTO.AppointmentResponse> queue = getDoctorQueue(doctorId, date);
        messagingTemplate.convertAndSend("/topic/queue/" + doctorId, queue);
    }

    private AppointmentDTO.AppointmentResponse mapToResponse(Appointment a) {
        return AppointmentDTO.AppointmentResponse.builder()
                .id(a.getId())
                .patientId(a.getPatient().getId())
                .patientName(a.getPatient().getUser().getName())
                .doctorId(a.getDoctor().getId())
                .doctorName(a.getDoctor().getUser().getName())
                .doctorSpecialization(a.getDoctor().getSpecialization())
                .appointmentDate(a.getAppointmentDate())
                .appointmentTime(a.getAppointmentTime())
                .status(a.getStatus().name())
                .symptoms(a.getSymptoms())
                .notes(a.getNotes())
                .queueNumber(a.getQueueNumber())
                .build();
    }
}
