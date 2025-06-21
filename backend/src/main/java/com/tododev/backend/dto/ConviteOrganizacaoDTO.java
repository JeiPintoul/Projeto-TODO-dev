package com.tododev.backend.dto;

import com.tododev.backend.model.Funcao;

public record ConviteOrganizacaoDTO(Long id, Long usuarioId, Long organizacaoId, String organizacaoNome, Funcao funcao, String status) {}
