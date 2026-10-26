package com.example.capstone.controller;

import com.example.capstone.exception.ResourceNotFoundException;
import com.example.capstone.model.Appointment;
import com.example.capstone.repository.AppointmentRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentRepository repository;
    public AppointmentController(AppointmentRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Appointment> getAll() {
        return repository.findAllByOrderByAppointmentDateAsc();
    }

    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment " + id + " was not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Appointment create(@Valid @RequestBody Appointment appointment) {
        appointment.setStatus("SCHEDULED");
        return repository.save(appointment);
    }

    @PutMapping("/{id}")
    public Appointment update(@PathVariable Long id, @Valid @RequestBody Appointment request) {
        Appointment current = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment " + id + " was not found"));
        current.setPatientName(request.getPatientName());
        current.setDoctorName(request.getDoctorName());
        current.setDepartment(request.getDepartment());
        current.setAppointmentDate(request.getAppointmentDate());
        current.setStatus(request.getStatus());
        return repository.save(current);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Appointment current = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment " + id + " was not found"));
        repository.delete(current);
    }
}
