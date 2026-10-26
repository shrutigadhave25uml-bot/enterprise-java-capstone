package com.example.capstone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 100)
    private String patientName;

    @NotBlank @Size(max = 100)
    private String doctorName;

    @NotBlank @Size(max = 100)
    private String department;

    @NotNull
    private LocalDate appointmentDate;

    @NotBlank
    private String status = "SCHEDULED";

    protected Appointment() {}

    public Appointment(String patientName, String doctorName, String department, LocalDate appointmentDate) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.department = department;
        this.appointmentDate = appointmentDate;
        this.status = "SCHEDULED";
    }
    public Long getId() { return id; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getDepartment() { return department; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public String getStatus() { return status; }
    public void setPatientName(String v) { patientName = v; }
    public void setDoctorName(String v) { doctorName = v; }
    public void setDepartment(String v) { department = v; }
    public void setAppointmentDate(LocalDate v) { appointmentDate = v; }
    public void setStatus(String v) { status = v; }
}
