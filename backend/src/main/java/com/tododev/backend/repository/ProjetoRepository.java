package com.tododev.backend.repository;

import com.tododev.backend.model.Projeto;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    List<Projeto> findByOrganizacaoId(Long organizacaoId);
}
