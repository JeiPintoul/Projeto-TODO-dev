package com.tododev.backend.dto;

import com.tododev.backend.model.Funcao;
import com.tododev.backend.model.Projeto;

import java.util.List;

public record ProjetoRespostaDTO(
    String id,
    String name,
    String description,
    String color,
    String status,
    String creationDate,
    String startDate,
    String dueDate,
    String artefacts,
    List<CompanyRespostaDTO> companies,
    List<UsuarioRespostaDTO> managers,
    List<UsuarioRespostaDTO> workers,
    List<TarefaRespostaDTO> tasks
) {
    public static ProjetoRespostaDTO fromEntity(Projeto projeto) {
        List<UsuarioRespostaDTO> managers = projeto.getUsuariosProjeto().stream()
            .filter(up -> up.getFuncao() != null && up.getFuncao() == Funcao.GERENTE)
            .map(up -> UsuarioRespostaDTO.fromEntity(up.getUsuario(), true)).toList();
        List<UsuarioRespostaDTO> workers = projeto.getUsuariosProjeto().stream()
            .filter(up -> up.getFuncao() == null || up.getFuncao() != Funcao.GERENTE)
            .map(up -> UsuarioRespostaDTO.fromEntity(up.getUsuario(), false)).toList();
        List<TarefaRespostaDTO> tasks = projeto.getTarefas() != null ? projeto.getTarefas().stream()
            .map(t -> new TarefaRespostaDTO(
                t.getId() != null ? t.getId().toString() : null,
                projeto.getId() != null ? projeto.getId().toString() : null,
                t.getTitulo(),
                t.getDescricao(),
                t.getStatus() != null ? t.getStatus().name().toLowerCase() : null,
                t.getPriority(),
                t.getColor(),
                t.getDataCriacao() != null ? t.getDataCriacao().toString() : null,
                t.getDataTermino() != null ? t.getDataTermino().toString() : null,
                t.getArtefacts()
            )).toList() : List.of();
        List<CompanyRespostaDTO> companies = projeto.getOrganizacoes() != null ? projeto.getOrganizacoes().stream()
            .map(org -> new CompanyRespostaDTO(
                org.getId() != null ? org.getId().toString() : null,
                org.getNome(),
                org.getDescricao()
            )).toList() : List.of();
        return new ProjetoRespostaDTO(
            projeto.getId() != null ? projeto.getId().toString() : null,
            projeto.getNome(),
            projeto.getDescricao(),
            projeto.getCor(),
            projeto.getStatus(),
            projeto.getDataCriacao() != null ? projeto.getDataCriacao().toString() : null,
            projeto.getDataInicio() != null ? projeto.getDataInicio().toString() : null,
            projeto.getDataVencimento() != null ? projeto.getDataVencimento().toString() : null,
            projeto.getArtefatos(),
            companies,
            managers,
            workers,
            tasks
        );
    }
}
