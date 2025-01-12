package com.epicode.it.tavel_manager.repository;

import com.epicode.it.tavel_manager.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByEmployeeIdAndRequestDate(Long employeeId, LocalDate requestDate);
    List<Reservation> findByTravelId(Long travelId);
}
