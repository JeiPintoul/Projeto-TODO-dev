package com.tododev.backend.repository;

import com.tododev.backend.model.ArtefatoTarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtefatoTarefaRepository extends JpaRepository<ArtefatoTarefa, Long> {
    List<ArtefatoTarefa> findByTaskId(Long taskId);
}
