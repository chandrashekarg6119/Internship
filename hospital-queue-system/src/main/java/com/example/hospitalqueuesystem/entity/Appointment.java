package com.example.hospitalqueuesystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User patient;

    @ManyToOne
    private Doctor doctor;

    private LocalDateTime appointmentTime;

    private int queueNumber;

    private String status;

    public Long getId(){ return id; }
    public void setId(Long id){ this.id=id; }

    public User getPatient(){ return patient; }
    public void setPatient(User patient){ this.patient=patient; }

    public Doctor getDoctor(){ return doctor; }
    public void setDoctor(Doctor doctor){ this.doctor=doctor; }

    public LocalDateTime getAppointmentTime(){ return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime){
        this.appointmentTime=appointmentTime;
    }

    public int getQueueNumber(){ return queueNumber; }
    public void setQueueNumber(int queueNumber){
        this.queueNumber=queueNumber;
    }

    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status=status; }

}