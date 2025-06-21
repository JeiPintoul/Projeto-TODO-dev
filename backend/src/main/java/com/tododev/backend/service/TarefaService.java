package com.tododev.backend.service;

import com.tododev.backend.dto.TarefaRespostaDTO;
import com.tododev.backend.exception.RecursoNaoEncontradoException;
import com.tododev.backend.model.*;
import com.tododev.backend.repository.*;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    private final UsuarioRepository usuarioRepository;

    private final ProjetoRepository projetoRepository;

    private final ArtefatoTarefaRepository artefatoTarefaRepository;

    public static final String MSG_TAREFA_NAO_ENCONTRADA = "Tarefa não encontrada com o ID: ";

    /**
     * Converte uma entidade Tarefa para o DTO de resposta, garantindo compatibilidade total com o frontend.
     */
    private TarefaRespostaDTO toTarefaRespostaDTO(Tarefa t) {
        return new TarefaRespostaDTO(
            t.getId() != null ? t.getId().toString() : null,
            t.getProjeto() != null ? t.getProjeto().getId().toString() : null,
            t.getTitulo(),
            t.getDescricao(),
            t.getStatus() != null ? t.getStatus().name().toLowerCase() : null,
            t.getPriority(),
            t.getColor(),
            t.getDataCriacao() != null ? t.getDataCriacao().toString() : null,
            t.getDataTermino() != null ? t.getDataTermino().toString() : null,
            t.getArtefacts() // Garante compatibilidade com o frontend
        );
    }

    /**
     * Cria uma nova tarefa e retorna o DTO de resposta compatível com o frontend.
     */
    public TarefaRespostaDTO criarTarefa(Long projetoId, Long gerenteId, String titulo, String descricao, String status, String priority, List<ArtefatoTarefa> artefatos) {
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto não encontrado com o ID: " + projetoId));
        Usuario gerente = usuarioRepository.findById(gerenteId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Gerente não encontrado com o ID: " + gerenteId));

        Tarefa tarefa = Tarefa.builder()
            .gerente(gerente)
            .projeto(projeto)
            .titulo(titulo)
            .descricao(descricao)
            .status(status != null ? StatusTarefa.fromString(status) : StatusTarefa.PENDENTE)
            .priority(priority)
            .dataCriacao(LocalDateTime.now())
            .build();

        // Salvar a tarefa antes de adicionar artefatos
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        // Adicionar artefatos se houverem
        if (artefatos != null && !artefatos.isEmpty()) {
            for (ArtefatoTarefa artefato : artefatos) {
                artefato.setTarefa(tarefaSalva);
                artefatoTarefaRepository.save(artefato);
            }
            tarefaSalva.setArtefatos(artefatos);
            tarefaSalva = tarefaRepository.save(tarefaSalva);
        }

        return toTarefaRespostaDTO(tarefaSalva);
    }

    /**
     * Lista todas as tarefas de um projeto, retornando DTOs compatíveis com o frontend.
     */
    public List<TarefaRespostaDTO> getTarefasPorProjeto(Long projetoId) {
        return tarefaRepository.findByProjetoId(projetoId).stream()
            .map(this::toTarefaRespostaDTO)
            .toList();
    }

    /**
     * Busca uma tarefa por ID, retornando o DTO de resposta se encontrada.
     */
    public Optional<TarefaRespostaDTO> getTarefaPorId(Long tarefaId) {
        return tarefaRepository.findById(tarefaId)
            .map(this::toTarefaRespostaDTO);
    }

    /**
     * Inicia uma tarefa, marcando o usuário responsável, e retorna o DTO atualizado.
     */
    public TarefaRespostaDTO iniciarTarefa(Long tarefaId, Long usuarioId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_TAREFA_NAO_ENCONTRADA + tarefaId));
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado com o ID: " + usuarioId));
        if (tarefa.getStatus() == StatusTarefa.EM_PROGRESSO || tarefa.getStatus() == StatusTarefa.CONCLUIDO) {
            throw new IllegalStateException("Tarefa já está em andamento ou finalizada.");
        }
        tarefa.setStatus(StatusTarefa.EM_PROGRESSO);
        tarefa.setUsuarioEmAndamento(usuario);
        tarefa.setDataTermino(null);
        tarefa.setTempoGasto(null);
        tarefaRepository.save(tarefa);
        return toTarefaRespostaDTO(tarefa);
    }

    /**
     * Marca uma tarefa como concluída, atualizando o usuário e datas, e retorna o DTO atualizado.
     */
    public TarefaRespostaDTO completarTarefa(Long tarefaId, Long usuarioId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_TAREFA_NAO_ENCONTRADA + tarefaId));
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado com o ID: " + usuarioId));
        tarefa.setStatus(StatusTarefa.CONCLUIDO);
        tarefa.setUsuarioConcluido(usuario);
        LocalDateTime agora = LocalDateTime.now();
        tarefa.setDataTermino(agora);
        if (tarefa.getDataCriacao() != null) {
            Duration duracao = Duration.between(tarefa.getDataCriacao(), agora);
            tarefa.setTempoGasto(duracao.toHours());
        } else {
            tarefa.setTempoGasto(null);
        }
        tarefaRepository.save(tarefa);
        return toTarefaRespostaDTO(tarefa);
    }

    /**
     * Edita os dados de uma tarefa e retorna o DTO atualizado.
     */
    public TarefaRespostaDTO editarTarefa(Long tarefaId, Long gerenteId, String titulo, String descricao, String status, String priority) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_TAREFA_NAO_ENCONTRADA + tarefaId));
        if (!tarefa.getGerente().getId().equals(gerenteId)) {
            throw new IllegalStateException("Apenas o gerente do projeto pode editar a tarefa.");
        }
        tarefa.setTitulo(titulo);
        tarefa.setDescricao(descricao);
        if (status != null) tarefa.setStatus(StatusTarefa.fromString(status));
        if (priority != null) tarefa.setPriority(priority);
        tarefaRepository.save(tarefa);
        return toTarefaRespostaDTO(tarefa);
    }

    /**
     * Deleta uma tarefa, apenas se o gerente for o responsável.
     */
    public void deletarTarefa(Long tarefaId, Long gerenteId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_TAREFA_NAO_ENCONTRADA + tarefaId));
        if (!tarefa.getGerente().getId().equals(gerenteId)) {
            throw new IllegalStateException("Apenas o gerente do projeto pode deletar a tarefa.");
        }
        tarefaRepository.delete(tarefa);
    }

    /**
     * Adiciona um artefato a uma tarefa existente.
     */
    public void adicionarArtefatoATarefa(Long tarefaId, ArtefatoTarefa artefato) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_TAREFA_NAO_ENCONTRADA + tarefaId));
        artefato.setTarefa(tarefa);
        artefato.setEditado(java.time.LocalDateTime.now());
        artefatoTarefaRepository.save(artefato);
    }

    /**
     * Lista todas as tarefas associadas a um usuário (em andamento ou concluídas), retornando DTOs compatíveis.
     */
    public List<TarefaRespostaDTO> getTarefasPorUsuario(Long usuarioId) {
        return tarefaRepository.findAll().stream()
            .filter(t -> (t.getUsuarioEmAndamento() != null && t.getUsuarioEmAndamento().getId().equals(usuarioId))
                || (t.getUsuarioConcluido() != null && t.getUsuarioConcluido().getId().equals(usuarioId)))
            .map(this::toTarefaRespostaDTO)
            .toList();
    }

}
