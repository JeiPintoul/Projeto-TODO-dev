package com.tododev.backend.repository;

import com.tododev.backend.model.TaskArtifact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskArtifactRepository extends JpaRepository<TaskArtifact, Long> {
    List<TaskArtifact> findByTaskId(Long taskId);
}
