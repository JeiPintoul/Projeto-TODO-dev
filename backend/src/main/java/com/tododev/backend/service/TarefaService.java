package com.tododev.backend.service;

import com.tododev.backend.dto.TarefaRespostaDTO;
import com.tododev.backend.dto.ArtefatoTarefaRespostaDTO;
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

    public Tarefa criarTarefa(Long projetoId, Long gerenteId, String titulo, String descricao, 
                            List<ArtefatoTarefa> artefatos) {

        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto não encontrado com o ID: " + projetoId));
        Usuario gerente = usuarioRepository.findById(gerenteId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Gerente não encontrado com o ID: " + gerenteId));

        Tarefa tarefa = Tarefa.builder()
            .gerente(gerente)
            .projeto(projeto)
            .titulo(titulo)
            .descricao(descricao)
            .status(StatusTarefa.PENDENTE)
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

        return tarefaSalva;
    }

    public List<Tarefa> getTarefasPorProjeto(Long projetoId) {
        return tarefaRepository.findByProjetoId(projetoId);
    }

    public Optional<Tarefa> getTarefaPorId(Long tarefaId) {
        return tarefaRepository.findById(tarefaId);
    }

    public Tarefa iniciarTarefa(Long tarefaId, Long usuarioId) {

        Tarefa tarefa = tarefaRepository.findById(tarefaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_TAREFA_NAO_ENCONTRADA + tarefaId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado com o ID: " + usuarioId));

        if (tarefa.getStatus() == StatusTarefa.EM_PROGRESSO || tarefa.getStatus() == StatusTarefa.CONCLUIDO) {
            throw new IllegalStateException("Tarefa já está em andamento ou finalizada.");
        }
        tarefa.setStatus(StatusTarefa.EM_PROGRESSO);
        tarefa.setUsuarioEmAndamento(usuario);
        // Optionally reset endDate and timeSpent when starting
        tarefa.setDataTermino(null);
        tarefa.setTempoGasto(null);

        // Save the task once after all updates
        tarefaRepository.save(tarefa);
        return tarefa;
    }

    public Tarefa completarTarefa(Long tarefaId, Long usuarioId) {

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
        return tarefa;

    }

    public Tarefa editarTarefa(Long tarefaId, Long gerenteId, String titulo, String descricao) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_TAREFA_NAO_ENCONTRADA + tarefaId));
        if (!tarefa.getGerente().getId().equals(gerenteId)) {
            throw new IllegalStateException("Apenas o gerente do projeto pode editar a tarefa.");
        }
        tarefa.setTitulo(titulo);
        tarefa.setDescricao(descricao);
        return tarefaRepository.save(tarefa);
    }

    public void deletarTarefa(Long tarefaId, Long gerenteId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_TAREFA_NAO_ENCONTRADA + tarefaId));
        if (!tarefa.getGerente().getId().equals(gerenteId)) {
            throw new IllegalStateException("Apenas o gerente do projeto pode deletar a tarefa.");
        }
        tarefaRepository.delete(tarefa);
    }

    public void adicionarArtefatoATarefa(Long tarefaId, ArtefatoTarefa artefato) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_TAREFA_NAO_ENCONTRADA + tarefaId));
        artefato.setTarefa(tarefa);
        artefato.setEditado(java.time.LocalDateTime.now());
        artefatoTarefaRepository.save(artefato);
    }

    public List<TarefaRespostaDTO> getTarefasPorUsuario(Long usuarioId) {
        return tarefaRepository.findAll().stream()
            .filter(t -> (t.getUsuarioEmAndamento() != null && t.getUsuarioEmAndamento().getId().equals(usuarioId))
                || (t.getUsuarioConcluido() != null && t.getUsuarioConcluido().getId().equals(usuarioId)))
            .map(t -> new TarefaRespostaDTO(
                t.getId(),
                t.getTitulo(),
                t.getDescricao(),
                t.getStatus() != null ? t.getStatus().name() : null,
                t.getDataCriacao(),
                t.getDataTermino(),
                t.getTempoGasto(),
                t.getProjeto() != null ? t.getProjeto().getId() : null,
                t.getGerente() != null ? t.getGerente().getId() : null,
                t.getUsuarioEmAndamento() != null ? t.getUsuarioEmAndamento().getId() : null,
                t.getUsuarioConcluido() != null ? t.getUsuarioConcluido().getId() : null,
                t.getArtefatos() != null ? t.getArtefatos().stream().map(a -> new ArtefatoTarefaRespostaDTO(
                    a.getId(), a.getConteudo(), a.getEditado(), a.getTipo()
                )).toList() : List.of()
            ))
            .toList();
    }

}
