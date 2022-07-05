package com.ey.inspectiontools.repository;

import com.ey.inspectiontools.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileManagerRepository extends JpaRepository<FileEntity, String> {
}
