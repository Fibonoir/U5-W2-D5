package com.epicode.it.tavel_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "travel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Destination cannot be blank")
    private String destination;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status cannot be null")
    private TravelStatus status;

}
