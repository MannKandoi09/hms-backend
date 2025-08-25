// Patient.java
package com.hms.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private String phone;
    private String email;
    private String address;
}
