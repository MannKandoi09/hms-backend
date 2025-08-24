package com.hms.hmsbackend.controller;

import com.hms.hmsbackend.entity.Patient;
import com.hms.hmsbackend.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody Patient p) {
        if (p.getUser() == null && (p.getDob() == null && p.getGender() == null
                && p.getBloodGroup() == null && p.getAddress() == null)) {
            // beginner-friendly: allow minimal record with just nullables; real app will validate
        }
        Patient saved = patientRepository.save(p);
        return ResponseEntity.ok(saved);
    }

    // READ all
    @GetMapping
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    // READ by id
    @GetMapping("/{id}")
    public ResponseEntity<Patient> findById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(@PathVariable Long id, @RequestBody Patient body) {
        return patientRepository.findById(id).map(p -> {
            p.setDob(body.getDob());
            p.setGender(body.getGender());
            p.setBloodGroup(body.getBloodGroup());
            p.setAddress(body.getAddress());
            Patient updated = patientRepository.save(p);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
