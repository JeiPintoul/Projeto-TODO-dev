package com.tododev.backend.controller;

import com.tododev.backend.model.Tarefa;
import com.tododev.backend.model.ArtefatoTarefa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.tododev.backend.service.TarefaService;

import lombok.RequiredArgsConstructor;
import com.tododev.backend.dto.TarefaRespostaDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/{organizacaoId}/{projetoId}/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    /**
     * Cria uma nova tarefa no projeto e retorna o DTO de resposta.
     * @param organizacaoId ID da organização
     * @param projetoId ID do projeto
     * @param usuarioId ID do usuário criador
     * @param tarefa Dados da tarefa recebidos do frontend
     * @return TarefaRespostaDTO compatível com o frontend
     */
    @PostMapping
    public ResponseEntity<TarefaRespostaDTO> criarTarefa(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @RequestParam Long usuarioId, @RequestBody Tarefa tarefa) {
        // Regra: só membros do projeto podem criar tarefa
        TarefaRespostaDTO nova = tarefaService.criarTarefa(
            projetoId,
            usuarioId,
            tarefa.getTitulo(),
            tarefa.getDescricao(),
            tarefa.getStatus() != null ? tarefa.getStatus().name() : null,
            tarefa.getPriority(),
            tarefa.getArtefatos()
        );
        return ResponseEntity.ok(nova);
    }

    /**
     * Edita uma tarefa existente e retorna o DTO atualizado.
     */
    @PutMapping("/{tarefaId}")
    public ResponseEntity<TarefaRespostaDTO> editarTarefa(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestParam Long usuarioId, @RequestBody Tarefa tarefa) {
        // Regra: só responsável ou gerente pode editar tarefa
        TarefaRespostaDTO editada = tarefaService.editarTarefa(
            tarefaId,
            usuarioId,
            tarefa.getTitulo(),
            tarefa.getDescricao(),
            tarefa.getStatus() != null ? tarefa.getStatus().name() : null,
            tarefa.getPriority()
        );
        return ResponseEntity.ok(editada);
    }

    /**
     * Deleta uma tarefa do projeto.
     */
    @DeleteMapping("/{tarefaId}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestParam Long usuarioId) {
        // Regra: só responsável ou gerente pode deletar tarefa
        tarefaService.deletarTarefa(tarefaId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Inicia uma tarefa, marcando o usuário responsável.
     */
    @PostMapping("/{tarefaId}/iniciar")
    public ResponseEntity<TarefaRespostaDTO> iniciarTarefa(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestParam Long usuarioId) {
        // Regra: só responsável pode iniciar tarefa
        TarefaRespostaDTO tarefa = tarefaService.iniciarTarefa(tarefaId, usuarioId);
        return ResponseEntity.ok(tarefa);
    }

    /**
     * Marca uma tarefa como concluída.
     */
    @PostMapping("/{tarefaId}/concluir")
    public ResponseEntity<TarefaRespostaDTO> concluirTarefa(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestParam Long usuarioId) {
        // Regra: só responsável pode concluir tarefa
        TarefaRespostaDTO tarefa = tarefaService.completarTarefa(tarefaId, usuarioId);
        return ResponseEntity.ok(tarefa);
    }

    /**
     * Adiciona um artefato a uma tarefa existente.
     */
    @PostMapping("/{tarefaId}/artefatos")
    public ResponseEntity<Void> adicionarArtefato(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestParam Long usuarioId, @RequestBody ArtefatoTarefa artefato) {
        // Regra: só responsável pode adicionar artefato
        tarefaService.adicionarArtefatoATarefa(tarefaId, artefato);
        return ResponseEntity.ok().build();
    }

    /**
     * Lista todas as tarefas do projeto, retornando DTOs compatíveis.
     */
    @GetMapping
    public ResponseEntity<List<TarefaRespostaDTO>> listarTarefas(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @RequestParam Long usuarioId) {
        // Regra: só membros do projeto podem listar tarefas
        List<TarefaRespostaDTO> tarefas = tarefaService.getTarefasPorProjeto(projetoId);
        return ResponseEntity.ok(tarefas);
    }

    /**
     * Busca uma tarefa por ID, retornando o DTO de resposta se encontrada.
     */
    @GetMapping("/{tarefaId}")
    public ResponseEntity<TarefaRespostaDTO> getTarefaPorId(@PathVariable Long organizacaoId, @PathVariable Long projetoId, @PathVariable Long tarefaId, @RequestParam Long usuarioId) {
        // Regra: só membros do projeto podem ver tarefa
        return tarefaService.getTarefaPorId(tarefaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
