package com.tododev.backend.dto;

import com.tododev.backend.model.Funcao;

public record UsuarioOrganizacaoRespostaDTO(
    Long usuarioId,
    String nome,
    String email,
    String apelido,
    Funcao funcao
) {}
