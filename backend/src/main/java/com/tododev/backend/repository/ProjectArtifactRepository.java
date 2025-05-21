package com.tododev.backend.repository;

import com.tododev.backend.model.ProjectArtifact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectArtifactRepository extends JpaRepository<ProjectArtifact, Long> {
    List<ProjectArtifact> findByProjectId(Long projectId);
}
