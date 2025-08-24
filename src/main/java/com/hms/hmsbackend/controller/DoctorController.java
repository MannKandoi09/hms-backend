package com.hms.hmsbackend.controller;

import com.hms.hmsbackend.entity.Department;
import com.hms.hmsbackend.entity.Doctor;
import com.hms.hmsbackend.repository.DepartmentRepository;
import com.hms.hmsbackend.repository.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public DoctorController(DoctorRepository doctorRepository, DepartmentRepository departmentRepository) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }

    // CREATE doctor (departmentId required)
    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long departmentId, @RequestBody Doctor d) {
        Department dept = departmentRepository.findById(departmentId).orElse(null);
        if (dept == null) {
            return ResponseEntity.badRequest().body("Invalid departmentId");
        }
        d.setDepartment(dept);
        Doctor saved = doctorRepository.save(d);
        return ResponseEntity.ok(saved);
    }

    // READ all
    @GetMapping
    public List<Doctor> all() {
        return doctorRepository.findAll();
    }

    // READ by dept
    @GetMapping("/by-department/{deptId}")
    public List<Doctor> byDepartment(@PathVariable Long deptId) {
        return doctorRepository.findByDepartmentId(deptId);
    }

    // READ by id
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> get(@PathVariable Long id) {
        return doctorRepository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam(required = false) Long departmentId,
                                    @RequestBody Doctor body) {
        return doctorRepository.findById(id).map(d -> {
            if (departmentId != null) {
                Department dept = departmentRepository.findById(departmentId).orElse(null);
                if (dept == null) return ResponseEntity.badRequest().body("Invalid departmentId");
                d.setDepartment(dept);
            }
            d.setSpecialization(body.getSpecialization());
            d.setExperienceYears(body.getExperienceYears());
            d.setFee(body.getFee());
            Doctor updated = doctorRepository.save(d);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
