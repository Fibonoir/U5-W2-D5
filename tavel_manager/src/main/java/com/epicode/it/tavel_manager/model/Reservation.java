package com.epicode.it.tavel_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "travel_id", nullable = false)
    @NotNull(message = "Travel refernce cannot be null")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Travel travel;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @NotNull(message = "Employee refernce cannot be null")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Employee employee;

    @NotNull(message = "Request date cannot be null")
    private LocalDate requestDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
}
