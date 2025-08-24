package com.hms.hmsbackend.controller;

import com.hms.hmsbackend.entity.Appointment;
import com.hms.hmsbackend.entity.Doctor;
import com.hms.hmsbackend.entity.Patient;
import com.hms.hmsbackend.repository.AppointmentRepository;
import com.hms.hmsbackend.repository.DoctorRepository;
import com.hms.hmsbackend.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentController(AppointmentRepository appointmentRepository,
                                 DoctorRepository doctorRepository,
                                 PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // BOOK appointment
    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestParam Long patientId,
                                  @RequestParam Long doctorId,
                                  @RequestParam String dateTime, // ISO: 2025-08-24T10:30:00
                                  @RequestParam(required = false) String notes) {
        Patient p = patientRepository.findById(patientId).orElse(null);
        if (p == null) return ResponseEntity.badRequest().body("Invalid patientId");

        Doctor d = doctorRepository.findById(doctorId).orElse(null);
        if (d == null) return ResponseEntity.badRequest().body("Invalid doctorId");

        LocalDateTime dt;
        try { dt = LocalDateTime.parse(dateTime); }
        catch (Exception e) { return ResponseEntity.badRequest().body("Invalid dateTime format. Use ISO like 2025-08-24T10:30:00"); }

        if (dt.isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Appointment must be in future");
        }

        if (appointmentRepository.existsByDoctorIdAndAppointmentDateTime(doctorId, dt)) {
            return ResponseEntity.badRequest().body("Doctor not available at this time");
        }

        Appointment a = new Appointment();
        a.setPatient(p);
        a.setDoctor(d);
        a.setAppointmentDateTime(dt);
        a.setStatus(Appointment.Status.BOOKED);
        a.setNotes(notes);

        Appointment saved = appointmentRepository.save(a);
        return ResponseEntity.ok(saved);
    }

    // Patient's appointments
    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<?> byPatient(@PathVariable Long patientId) {
        if (!patientRepository.existsById(patientId)) return ResponseEntity.badRequest().body("Invalid patientId");
        return ResponseEntity.ok(appointmentRepository.findByPatientIdOrderByAppointmentDateTimeDesc(patientId));
    }

    // Doctor's today's appointments
    @GetMapping("/doctor-today/{doctorId}")
    public ResponseEntity<?> doctorToday(@PathVariable Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) return ResponseEntity.badRequest().body("Invalid doctorId");
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay().minusNanos(1);
        List<Appointment> list = appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctorId, start, end);
        return ResponseEntity.ok(list);
    }

    // Update status (COMPLETE/CANCEL)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return appointmentRepository.findById(id).map(a -> {
            try {
                Appointment.Status st = Appointment.Status.valueOf(status.toUpperCase());
                a.setStatus(st);
                return ResponseEntity.ok(appointmentRepository.save(a));
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body("Invalid status. Use BOOKED, COMPLETED or CANCELED");
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // Cancel (delete) appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
