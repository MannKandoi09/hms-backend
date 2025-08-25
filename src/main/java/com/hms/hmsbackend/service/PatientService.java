package com.hms.hmsbackend.service;

import com.hms.hmsbackend.entity.Patient;
import com.hms.hmsbackend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepo.findById(id);
    }

    public Patient savePatient(Patient patient) {
        return patientRepo.save(patient);
    }

    public void deletePatient(Long id) {
        patientRepo.deleteById(id);
    }
}
