package com.tododev.backend.dto;

import com.tododev.backend.model.Funcao;
import jakarta.validation.constraints.NotNull;

public record AdicionarUsuarioOrganizacaoDTO(
    @NotNull(message = "O id do usuário é obrigatório")
    Long usuarioId,
    @NotNull(message = "A função é obrigatória")
    Funcao funcao
) {}
