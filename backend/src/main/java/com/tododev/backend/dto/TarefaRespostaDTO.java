package com.tododev.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TarefaRespostaDTO(
    Long id,
    String titulo,
    String descricao,
    String status,
    LocalDateTime dataCriacao,
    LocalDateTime dataTermino,
    Long tempoGasto,
    Long projetoId,
    Long gerenteId,
    Long usuarioEmAndamentoId,
    Long usuarioConcluidoId,
    List<ArtefatoTarefaRespostaDTO> artefatos
) {}
