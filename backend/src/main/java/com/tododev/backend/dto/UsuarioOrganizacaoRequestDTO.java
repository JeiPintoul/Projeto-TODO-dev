package com.tododev.backend.dto;

import com.tododev.backend.model.Funcao;

public record UsuarioOrganizacaoRequestDTO(
    Long usuarioId,
    Funcao funcao
) {}
