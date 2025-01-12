package com.epicode.it.tavel_manager.controller;

import com.epicode.it.tavel_manager.model.Travel;
import com.epicode.it.tavel_manager.model.TravelStatus;
import com.epicode.it.tavel_manager.service.TravelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping
    public List<Travel> getAllTravels() {
        return travelService.getAllTravels();
    }

    @GetMapping("/{id}")
    public Travel getTravel(@PathVariable Long id) {
        return travelService.getTravelById(id);
    }

    @GetMapping("/paged")
    public Page<Travel> getTravelsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return travelService.getTravelsPaginated(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Travel> createTravel(@RequestBody Travel travel) {
        Travel createdTravel = travelService.createTravel(travel);
        return ResponseEntity.ok(createdTravel);
    }

    @PutMapping("/{id}")
    public Travel updateTravel(@PathVariable Long id, @RequestBody Travel travel) {
        return travelService.updateTravel(id, travel);
    }

    @PutMapping("/{id}/status")
    public Travel changeStatus(@PathVariable Long id, @RequestParam TravelStatus status) {
        return travelService.changeStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTravel(@PathVariable Long id) {
        travelService.deleteTravel(id);
    }
}
