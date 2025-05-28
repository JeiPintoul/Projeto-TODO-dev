package com.tododev.backend.service;

import com.tododev.backend.exception.RecursoNaoEncontradoException;
import com.tododev.backend.model.*;
import com.tododev.backend.repository.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    private final UsuarioRepository usuarioRepository;

    private final ProjetoRepository projetoRepository;

    private final ArtefatoTarefaRepository artefatoTarefaRepository;

    public Tarefa criarTarefa(Long projetoId, Long gerenteId, String titulo, String descricao, 
                            List<ArtefatoTarefa> artefatos) {

        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto não encontrado."));
        Usuario gerente = usuarioRepository.findById(gerenteId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Gerente não encontrado."));

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
        return tarefaRepository.findByProjectId(projetoId);
    }

    public Optional<Tarefa> getTarefaPorId(Long tarefaId) {
        return tarefaRepository.findById(tarefaId);
    }

    public Tarefa iniciarTarefa(Long tarefaId, Long usuarioId) {

        Tarefa tarefa = tarefaRepository.findById(tarefaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada com o ID: " + tarefaId));

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
            .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada com o ID: " + tarefaId));

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

    // Additional methods for updating, deleting tasks, adding artifacts, etc.
    //evolution API, Supabase, Lovable, n8n
}
