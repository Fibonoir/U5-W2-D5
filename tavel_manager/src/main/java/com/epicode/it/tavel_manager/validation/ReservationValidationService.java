package com.epicode.it.tavel_manager.validation;

import com.epicode.it.tavel_manager.exception.ReservationConflictException;
import com.epicode.it.tavel_manager.model.Reservation;
import com.epicode.it.tavel_manager.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationValidationService {

    public final ReservationRepository reservationRepository;

    public ReservationValidationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void validateNewReservation(Reservation reservation){
        List<Reservation> existingReservations =
            reservationRepository.findByEmployeeIdAndRequestDate(
                    reservation.getEmployee().getId(),
                    reservation.getRequestDate()
            );

        if(!existingReservations.isEmpty()){
            throw new ReservationConflictException("Employee already has a reservation for the requested date");
        }
    }

    
}
