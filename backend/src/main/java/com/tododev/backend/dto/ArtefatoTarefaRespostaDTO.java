package com.tododev.backend.dto;

import java.time.LocalDateTime;

public record ArtefatoTarefaRespostaDTO(
    Long id,
    String conteudo,
    LocalDateTime editado,
    String tipo
) {}
