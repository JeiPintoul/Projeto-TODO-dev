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
@RequestMapping("/api/{organizacaoId}/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<ProjetoResumoDTO> criarProjeto(@PathVariable Long organizacaoId, @RequestBody @Valid CriarProjetoDTO projetoDTO) {
        try {
            Projeto projeto = projetoService.criarProjeto(
                organizacaoId,
                projetoDTO.gerenteId(),
                projetoDTO.nome(),
                projetoDTO.descricao()
            );
            return ResponseEntity.ok(new ProjetoResumoDTO(projeto.getId(), projeto.getNome()));
        } catch (com.tododev.backend.exception.RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjetoResumoDTO>> getProjetos(@PathVariable Long organizacaoId) {
        List<ProjetoResumoDTO> projects = projetoService.getProjetosPorOrganizacao(organizacaoId)
            .stream().map(p -> new ProjetoResumoDTO(p.getId(), p.getNome())).toList();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projetoId}")
    public ResponseEntity<ProjetoResumoDTO> getProjetoPorId(@PathVariable Long organizacaoId, @PathVariable Long projetoId) {
        // Opcional: validar se o projeto pertence à organização
        return projetoService.getProjetoPorId(projetoId)
                .filter(p -> p.getOrganizacao().getId().equals(organizacaoId))
                .map(p -> ResponseEntity.ok(new ProjetoResumoDTO(p.getId(), p.getNome())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{projetoId}")
    public ResponseEntity<ProjetoResumoDTO> atualizarProjeto(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @RequestBody @Valid AtualizarProjetoDTO dto) {
        Projeto atualizado = projetoService.atualizarProjeto(projetoId, dto);
        if (!atualizado.getOrganizacao().getId().equals(organizacaoId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ProjetoResumoDTO(atualizado.getId(), atualizado.getNome()));
    }

    @PostMapping("/{projetoId}/membros")
    public ResponseEntity<Void> adicionarMembros(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @RequestBody @Valid List<AdicionarMembroProjetoDTO> membros) {
        Projeto projeto = projetoService.getProjetoPorId(projetoId).orElse(null);
        if (projeto == null || !projeto.getOrganizacao().getId().equals(organizacaoId)) {
            return ResponseEntity.notFound().build();
        }
        projetoService.adicionarMembrosAoProjeto(projetoId, membros);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projetoId}/artefatos")
    public ResponseEntity<Void> adicionarArtefato(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @RequestBody @Valid AdicionarArtefatoProjetoDTO dto) {
        Projeto projeto = projetoService.getProjetoPorId(projetoId).orElse(null);
        if (projeto == null || !projeto.getOrganizacao().getId().equals(organizacaoId)) {
            return ResponseEntity.notFound().build();
        }
        projetoService.adicionarArtefatoAoProjeto(projetoId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projetoId}")
    public ResponseEntity<Void> deletarProjeto(@PathVariable Long organizacaoId, @PathVariable Long projetoId) {
        Projeto projeto = projetoService.getProjetoPorId(projetoId).orElse(null);
        if (projeto == null || !projeto.getOrganizacao().getId().equals(organizacaoId)) {
            return ResponseEntity.notFound().build();
        }
        projetoService.deletarProjeto(projetoId);
        return ResponseEntity.ok().build();
    }
    
}
