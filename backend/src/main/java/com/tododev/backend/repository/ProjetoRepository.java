package com.tododev.backend.repository;

import com.tododev.backend.model.Projeto;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    // Busca projetos que pertencem a uma organização específica
    List<Projeto> findByOrganizacoes_Id(Long organizacaoId);
}
