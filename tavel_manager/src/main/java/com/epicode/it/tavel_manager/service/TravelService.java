package com.epicode.it.tavel_manager.service;

import com.epicode.it.tavel_manager.exception.ResourceNotFoundException;
import com.epicode.it.tavel_manager.model.Reservation;
import com.epicode.it.tavel_manager.model.Travel;
import com.epicode.it.tavel_manager.model.TravelStatus;
import com.epicode.it.tavel_manager.repository.ReservationRepository;
import com.epicode.it.tavel_manager.repository.TravelRepository;
import com.epicode.it.tavel_manager.validation.ValidationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;


import java.util.List;

@Service
@Transactional
public class TravelService {

    private final TravelRepository travelRepository;
    private final ReservationRepository reservationRepository;
    private final ValidationService validationService;

    public TravelService(TravelRepository travelRepository, ValidationService validationService, ReservationRepository reservationRepository) {
        this.travelRepository = travelRepository;
        this.validationService = validationService;
        this.reservationRepository = reservationRepository;
    }


    public Travel createTravel(Travel travel) {
        validationService.validate(travel);
        return travelRepository.save(travel);
    }

    @Transactional(readOnly = true)
    public Travel getTravelById(Long id) {
        return travelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Travel not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Travel> getTravelsPaginated(Pageable pageable) {
        return travelRepository.findAll(pageable);
    }


    public Travel updateTravel(Long id, Travel updatedTravel) {
        Travel existingTravel = getTravelById(id);

        existingTravel.setDestination(updatedTravel.getDestination());
        existingTravel.setDate(updatedTravel.getDate());
        existingTravel.setStatus(updatedTravel.getStatus());

        validationService.validate(existingTravel);
        return travelRepository.save(existingTravel);
    }


    public void deleteTravel(Long id) {
        List<Reservation> reservations = reservationRepository.findByTravelId(id);
        reservationRepository.deleteAll(reservations);
        travelRepository.deleteById(id);
    }


    public Travel changeStatus(Long id, TravelStatus status) {
        Travel travel = getTravelById(id);
        travel.setStatus(status);
        return travelRepository.save(travel);
    }
}
