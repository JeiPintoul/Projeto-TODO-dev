package com.tododev.backend.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarProjetoDTO(
    @NotBlank(message = "O nome do projeto é obrigatório")
    String nome,
    String descricao,
    @NotNull(message = "Status é obrigatório")
    String status,
    LocalDateTime dataTermino
) {}
