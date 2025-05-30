package com.tododev.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record AdicionarArtefatoProjetoDTO(
    @NotBlank(message = "O conteúdo do artefato é obrigatório")
    String conteudo,
    @NotBlank(message = "O tipo do artefato é obrigatório")
    String tipo
) {}
