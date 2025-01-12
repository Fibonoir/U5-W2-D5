package com.epicode.it.tavel_manager.repository;

import com.epicode.it.tavel_manager.model.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    Optional<ProfilePicture> findByEmployeeId(Long employeeId);
}
