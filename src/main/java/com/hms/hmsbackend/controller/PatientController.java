package com.hms.hmsbackend.controller;

import com.hms.hmsbackend.entity.Patient;
import com.hms.hmsbackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public List<Patient> getAll() { return patientService.getAllPatients(); }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getById(@PathVariable Long id){
        return patientService.getPatientById(id)
                .map(p -> ResponseEntity.ok(p))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Patient create(@RequestBody Patient p){ return patientService.savePatient(p); }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(@PathVariable Long id, @RequestBody Patient p){
        return patientService.getPatientById(id)
                .map(existing -> {
                    existing.setName(p.getName());
                    existing.setAge(p.getAge());
                    existing.setPhone(p.getPhone());
                    existing.setEmail(p.getEmail());
                    existing.setAddress(p.getAddress());
                    return ResponseEntity.ok(patientService.savePatient(existing));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if(patientService.getPatientById(id).isPresent()){
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
