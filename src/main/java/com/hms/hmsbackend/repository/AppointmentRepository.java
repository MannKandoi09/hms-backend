package com.hms.hmsbackend.repository;

import com.hms.hmsbackend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndAppointmentDateTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);
    boolean existsByDoctorIdAndAppointmentDateTime(Long doctorId, LocalDateTime dateTime);
    List<Appointment> findByPatientIdOrderByAppointmentDateTimeDesc(Long patientId);
}
