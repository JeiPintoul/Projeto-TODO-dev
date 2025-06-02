package com.tododev.backend.repository;

import com.tododev.backend.model.ArtefatoProjeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtefatoProjetoRepository extends JpaRepository<ArtefatoProjeto, Long> {
    List<ArtefatoProjeto> findByProjetoId(Long projetoId);
}
