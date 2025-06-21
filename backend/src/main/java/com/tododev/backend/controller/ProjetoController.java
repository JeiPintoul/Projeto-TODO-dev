package com.tododev.backend.controller;

import com.tododev.backend.dto.*;
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
    public ResponseEntity<ProjetoRespostaDTO> criarProjeto(@RequestParam Long usuarioId, @RequestBody @Valid CriarProjetoDTO projetoDTO) {
        ProjetoRespostaDTO projeto = projetoService.criarProjeto(projetoDTO, usuarioId);
        return ResponseEntity.ok(projeto);
    }

    @GetMapping("/{projetoId}")
    public ResponseEntity<ProjetoRespostaDTO> getProjetoPorId(@PathVariable Long projetoId, @RequestParam Long usuarioId) {
        return projetoService.getProjetoPorId(projetoId, usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{projetoId}")
    public ResponseEntity<ProjetoRespostaDTO> atualizarProjeto(@PathVariable Long projetoId, @RequestParam Long usuarioId, @RequestBody @Valid AtualizarProjetoDTO dto) {
        ProjetoRespostaDTO atualizado = projetoService.atualizarProjeto(projetoId, usuarioId, dto);
        return ResponseEntity.ok(atualizado);
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
