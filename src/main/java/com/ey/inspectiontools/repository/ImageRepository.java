package com.ey.inspectiontools.repository;

import com.ey.inspectiontools.model.checklist.ImageEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEvidence, Long> {
    Optional<ImageEvidence> findByName(String name);
}
