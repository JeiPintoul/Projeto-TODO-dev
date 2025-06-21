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

    @PostMapping
    public ResponseEntity<ProjetoResumoDTO> criarProjeto(@RequestParam Long usuarioId, @RequestBody @Valid CriarProjetoDTO projetoDTO) {
        Projeto projeto = projetoService.criarProjeto(projetoDTO, usuarioId);
        return ResponseEntity.ok(new ProjetoResumoDTO(projeto.getId(), projeto.getNome()));
    }

    @GetMapping("/{projetoId}")
    public ResponseEntity<ProjetoResumoDTO> getProjetoPorId(@PathVariable Long projetoId, @RequestParam Long usuarioId) {
        // Regra: só membros do projeto podem visualizar
        return projetoService.getProjetoPorId(projetoId, usuarioId)
                .map(p -> ResponseEntity.ok(new ProjetoResumoDTO(p.getId(), p.getNome())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{projetoId}")
    public ResponseEntity<ProjetoResumoDTO> atualizarProjeto(@PathVariable Long projetoId, @RequestParam Long usuarioId, @RequestBody @Valid AtualizarProjetoDTO dto) {
        // Regra: só gerente pode atualizar
        Projeto atualizado = projetoService.atualizarProjeto(projetoId, usuarioId, dto);
        return ResponseEntity.ok(new ProjetoResumoDTO(atualizado.getId(), atualizado.getNome()));
    }

    @PostMapping("/{projetoId}/membros")
    public ResponseEntity<Void> adicionarMembros(@PathVariable Long projetoId, @RequestParam Long usuarioId, @RequestBody @Valid List<AdicionarMembroProjetoDTO> membros) {
        // Regra: só gerente pode adicionar membros
        projetoService.adicionarMembrosAoProjeto(projetoId, usuarioId, membros);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projetoId}/artefatos")
    public ResponseEntity<Void> adicionarArtefato(@PathVariable Long projetoId, @RequestParam Long usuarioId, @RequestBody @Valid AdicionarArtefatoProjetoDTO dto) {
        // Regra: só membros do projeto podem adicionar artefatos
        projetoService.adicionarArtefatoAoProjeto(projetoId, usuarioId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projetoId}")
    public ResponseEntity<Void> deletarProjeto(@PathVariable Long projetoId, @RequestParam Long usuarioId) {
        // Regra: só gerente pode deletar
        projetoService.deletarProjeto(projetoId, usuarioId);
        return ResponseEntity.ok().build();
    }
    
}
