package com.tododev.backend.controller;

import com.tododev.backend.model.Tarefa;
import com.tododev.backend.model.ArtefatoTarefa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.tododev.backend.service.TarefaService;

import lombok.RequiredArgsConstructor;
import com.tododev.backend.dto.TarefaRespostaDTO;
import com.tododev.backend.dto.ArtefatoTarefaRespostaDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/{organizacaoId}/{projetoId}/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaRespostaDTO> criarTarefa(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @RequestParam Long gerenteId, @RequestBody Tarefa tarefa) {
        Tarefa nova = tarefaService.criarTarefa(projetoId, gerenteId, tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getArtefatos());
        return ResponseEntity.ok(toDTO(nova));
    }

    @PutMapping("/{tarefaId}")
    public ResponseEntity<TarefaRespostaDTO> editarTarefa(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestParam Long gerenteId, @RequestBody Tarefa tarefa) {
        Tarefa editada = tarefaService.editarTarefa(tarefaId, gerenteId, tarefa.getTitulo(), tarefa.getDescricao());
        return ResponseEntity.ok(toDTO(editada));
    }

    @DeleteMapping("/{tarefaId}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestParam Long gerenteId) {
        tarefaService.deletarTarefa(tarefaId, gerenteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tarefaId}/iniciar")
    public ResponseEntity<TarefaRespostaDTO> iniciarTarefa(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestParam Long usuarioId) {
        Tarefa tarefa = tarefaService.iniciarTarefa(tarefaId, usuarioId);
        return ResponseEntity.ok(toDTO(tarefa));
    }

    @PostMapping("/{tarefaId}/concluir")
    public ResponseEntity<TarefaRespostaDTO> concluirTarefa(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestParam Long usuarioId) {
        Tarefa tarefa = tarefaService.completarTarefa(tarefaId, usuarioId);
        return ResponseEntity.ok(toDTO(tarefa));
    }

    @PostMapping("/{tarefaId}/artefatos")
    public ResponseEntity<Void> adicionarArtefato(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestBody ArtefatoTarefa artefato) {
        tarefaService.adicionarArtefatoATarefa(tarefaId, artefato);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TarefaRespostaDTO>> listarTarefas(@PathVariable Long organizacaoId, @PathVariable Long projetoId) {
        List<TarefaRespostaDTO> tarefas = tarefaService.getTarefasPorProjeto(projetoId)
            .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{tarefaId}")
    public ResponseEntity<TarefaRespostaDTO> getTarefaPorId(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId) {
        return tarefaService.getTarefaPorId(tarefaId)
                .map(t -> ResponseEntity.ok(toDTO(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    private TarefaRespostaDTO toDTO(Tarefa tarefa) {
        List<ArtefatoTarefaRespostaDTO> artefatos = tarefa.getArtefatos() != null ?
            tarefa.getArtefatos().stream().map(a -> new ArtefatoTarefaRespostaDTO(
                a.getId(),
                a.getConteudo(),
                a.getEditado(),
                a.getTipo()
            )).toList() : List.of();
        return new TarefaRespostaDTO(
            tarefa.getId(),
            tarefa.getTitulo(),
            tarefa.getDescricao(),
            tarefa.getStatus() != null ? tarefa.getStatus().name() : null,
            tarefa.getDataCriacao(),
            tarefa.getDataTermino(),
            tarefa.getTempoGasto(),
            tarefa.getProjeto() != null ? tarefa.getProjeto().getId() : null,
            tarefa.getGerente() != null ? tarefa.getGerente().getId() : null,
            tarefa.getUsuarioEmAndamento() != null ? tarefa.getUsuarioEmAndamento().getId() : null,
            tarefa.getUsuarioConcluido() != null ? tarefa.getUsuarioConcluido().getId() : null,
            artefatos
        );
    }
}
