package com.tododev.backend.repository;

import com.tododev.backend.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByProjectId(Long projectId);
}
