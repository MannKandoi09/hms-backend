package com.hms.hmsbackend.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    private Status status = Status.BOOKED;

    private String notes;

    public enum Status {
        BOOKED,
        COMPLETED,
        CANCELED
    }
}

