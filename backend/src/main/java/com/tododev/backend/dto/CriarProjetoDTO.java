package com.tododev.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.tododev.backend.model.Projeto;

public record CriarProjetoDTO(
    @NotBlank(message = "O nome do projeto é obrigatório")
    String nome,
    String descricao,
    @NotNull(message = "O id da organização é obrigatório")
    Long organizacaoId,
    @NotNull(message = "O id do gerente é obrigatório")
    Long gerenteId
) {
    // Converte DTO para entidade Projeto (sem setar campos controlados pelo backend)
    public Projeto paraEntidade() {
        Projeto projeto = new Projeto();
        projeto.setNome(this.nome);
        projeto.setDescricao(this.descricao);
        // organizacao, gerente, status, datas devem ser setados no service
        return projeto;
    }
}
