package com.tododev.backend.controller;

import com.tododev.backend.dto.CriarProjetoDTO;
import com.tododev.backend.model.Projeto;
import com.tododev.backend.service.ProjetoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    // No ProjetoController.java
    @PostMapping
    public ResponseEntity<Projeto> criarProjeto(@RequestBody @Valid CriarProjetoDTO projetoDTO) {
        Projeto projeto = projetoService.criarProjeto(
            projetoDTO.organizacaoId(), 
            projetoDTO.gerenteId(), 
            projetoDTO.nome(), 
            projetoDTO.descricao()
        );
        return ResponseEntity.ok(projeto);
    }

    @GetMapping("/organizacao/{organizacaoId}")
    public ResponseEntity<List<Projeto>> getProjetosPorOrganizacao(@PathVariable Long organizacaoId) {
        List<Projeto> projects = projetoService.getProjetosPorOrganizacao(organizacaoId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projetoId}")
    public ResponseEntity<Projeto> getProjectById(@PathVariable Long projetoId) {
        return projetoService.getProjetoPorId(projetoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
