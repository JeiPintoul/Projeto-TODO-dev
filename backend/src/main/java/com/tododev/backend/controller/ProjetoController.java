package com.tododev.backend.controller;

import com.tododev.backend.dto.*;
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
    public ResponseEntity<ProjetoResumoDTO> criarProjeto(@RequestBody @Valid CriarProjetoDTO projetoDTO) {
        Projeto projeto = projetoService.criarProjeto(
            projetoDTO.organizacaoId(),
            projetoDTO.gerenteId(),
            projetoDTO.nome(),
            projetoDTO.descricao()
        );
        return ResponseEntity.ok(new ProjetoResumoDTO(projeto.getId(), projeto.getNome()));
    }

    @GetMapping("/organizacao/{organizacaoId}")
    public ResponseEntity<List<ProjetoResumoDTO>> getProjetosPorOrganizacao(@PathVariable Long organizacaoId) {
        List<ProjetoResumoDTO> projects = projetoService.getProjetosPorOrganizacao(organizacaoId)
            .stream().map(p -> new ProjetoResumoDTO(p.getId(), p.getNome())).toList();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projetoId}")
    public ResponseEntity<ProjetoResumoDTO> getProjetoPorId(@PathVariable Long projetoId) {
        return projetoService.getProjetoPorId(projetoId)
                .map(p -> ResponseEntity.ok(new ProjetoResumoDTO(p.getId(), p.getNome())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{projetoId}")
    public ResponseEntity<ProjetoResumoDTO> atualizarProjeto(@PathVariable Long projetoId, @RequestBody @Valid AtualizarProjetoDTO dto) {
        Projeto atualizado = projetoService.atualizarProjeto(projetoId, dto);
        return ResponseEntity.ok(new ProjetoResumoDTO(atualizado.getId(), atualizado.getNome()));
    }

    @PostMapping("/{projetoId}/membros")
    public ResponseEntity<Void> adicionarMembros(@PathVariable Long projetoId, @RequestBody @Valid List<AdicionarMembroProjetoDTO> membros) {
        projetoService.adicionarMembrosAoProjeto(projetoId, membros);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projetoId}/artefatos")
    public ResponseEntity<Void> adicionarArtefato(@PathVariable Long projetoId, @RequestBody @Valid AdicionarArtefatoProjetoDTO dto) {
        projetoService.adicionarArtefatoAoProjeto(projetoId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projetoId}")
    public ResponseEntity<Void> deletarProjeto(@PathVariable Long projetoId) {
        projetoService.deletarProjeto(projetoId);
        return ResponseEntity.ok().build();
    }
    
}
