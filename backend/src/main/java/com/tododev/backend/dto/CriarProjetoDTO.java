package com.tododev.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarProjetoDTO(
    @NotBlank(message = "O nome do projeto é obrigatório")
    String nome,
    String descricao,
    @NotNull(message = "O id do gerente é obrigatório")
    Long gerenteId
) {
}
