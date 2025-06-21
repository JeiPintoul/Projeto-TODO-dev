package com.tododev.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CriarProjetoDTO(
    @NotBlank(message = "O nome do projeto é obrigatório")
    String nome,
    String descricao,
    String cor,
    String status,
    String dataInicio,
    String dataVencimento,
    String artefatos,
    @NotNull(message = "Lista de organizações é obrigatória")
    List<Long> companyIds,
    @NotNull(message = "Lista de gerentes é obrigatória")
    List<Long> managerIds,
    @NotNull(message = "Lista de trabalhadores é obrigatória")
    List<Long> workerIds
) {}
