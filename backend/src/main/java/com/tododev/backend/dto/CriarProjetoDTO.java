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
    List<CompanyData> companies,
    @NotNull(message = "Lista de gerentes é obrigatória")
    List<UserData> managers,
    @NotNull(message = "Lista de trabalhadores é obrigatória")
    List<UserData> workers
) {
    public record CompanyData(String id, String name, String description) {}
    public record UserData(String id, String name) {}
}
