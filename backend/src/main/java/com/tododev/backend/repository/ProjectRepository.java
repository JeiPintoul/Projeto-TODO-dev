package com.tododev.backend.repository;

import com.tododev.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOrganizacaoId(Long organizacaoId);
}
