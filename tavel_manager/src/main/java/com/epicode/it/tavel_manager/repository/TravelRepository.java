package com.epicode.it.tavel_manager.repository;

import com.epicode.it.tavel_manager.model.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<Travel, Long> {
}
