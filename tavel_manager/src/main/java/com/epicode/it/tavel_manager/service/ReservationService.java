package com.epicode.it.tavel_manager.service;

import com.epicode.it.tavel_manager.exception.ResourceNotFoundException;
import com.epicode.it.tavel_manager.model.Employee;
import com.epicode.it.tavel_manager.model.Reservation;
import com.epicode.it.tavel_manager.model.Travel;
import com.epicode.it.tavel_manager.repository.EmployeeRepository;
import com.epicode.it.tavel_manager.repository.ReservationRepository;
import com.epicode.it.tavel_manager.repository.TravelRepository;
import com.epicode.it.tavel_manager.validation.ReservationValidationService;
import com.epicode.it.tavel_manager.validation.ValidationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final EmployeeRepository employeeRepository;
    private final TravelRepository travelRepository;
    private final ReservationValidationService reservationValidationService;
    private final ValidationService validationService;

    public ReservationService(ReservationRepository reservationRepository, EmployeeRepository employeeRepository, TravelRepository travelRepository, ReservationValidationService reservationValidationService, ValidationService validationService) {
        this.reservationRepository = reservationRepository;
        this.employeeRepository = employeeRepository;
        this.travelRepository = travelRepository;
        this.reservationValidationService = reservationValidationService;
        this.validationService = validationService;
    }

    public Reservation createReservation(Long employeeId,Long travelId,Reservation reservation) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel not found with id: " + travelId));

        reservation.setEmployee(employee);
        reservation.setTravel(travel);
        reservation.setDescription(reservation.getDescription());

        reservationValidationService.validateNewReservation(reservation);

        validationService.validate(reservation);

        return reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Reservation> getReservationsPaginated(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        Reservation existingReservation = getReservationById(id);

        if (updatedReservation.getEmployee() != null) {
            Employee employee = employeeRepository.findById(updatedReservation.getEmployee().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + updatedReservation.getEmployee().getId()));
            existingReservation.setEmployee(employee);
        }
        if (updatedReservation.getTravel() != null) {
            Travel travel = travelRepository.findById(updatedReservation.getTravel().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Travel not found with id: " + updatedReservation.getTravel().getId()));
            existingReservation.setTravel(travel);
        }

        existingReservation.setRequestDate(updatedReservation.getRequestDate());
        existingReservation.setDescription(updatedReservation.getDescription());

        if (updatedReservation.getEmployee() != null || !existingReservation.getRequestDate().equals(updatedReservation.getRequestDate())) {
            reservationValidationService.validateNewReservation(existingReservation);
        }

        validationService.validate(existingReservation);

        return reservationRepository.save(existingReservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}