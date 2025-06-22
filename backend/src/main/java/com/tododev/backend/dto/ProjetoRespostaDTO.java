package com.tododev.backend.dto;

import com.tododev.backend.model.Funcao;
import com.tododev.backend.model.Projeto;

import java.util.List;

public record ProjetoRespostaDTO(
    String status
) {
    public static ProjetoRespostaDTO fromEntity(Projeto projeto) {
        return new ProjetoRespostaDTO("ok");
    }
}
