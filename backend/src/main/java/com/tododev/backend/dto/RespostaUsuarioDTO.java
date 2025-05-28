package com.tododev.backend.dto;

import java.util.List;
import com.tododev.backend.model.Usuario;

public record RespostaUsuarioDTO(
    Long id,
    String nome,
    String email,
    String apelido,
    List<OrganizacaoResumoDTO> organizacoes,
    List<ProjetoResumoDTO> projetos
) {

    public static RespostaUsuarioDTO daEntidade(Usuario usuario) {

        List<OrganizacaoResumoDTO> orgs = usuario.getOrganizacoes() != null ?
            usuario.getOrganizacoes().stream()
                .map(org -> new OrganizacaoResumoDTO(org.getId(), org.getNome()))
                .toList() : List.of();
        List<ProjetoResumoDTO> projs = usuario.getProjectUsers() != null ?
            usuario.getProjectUsers().stream()
                .map(up -> new ProjetoResumoDTO(up.getProjeto().getId(), up.getProjeto().getNome()))
                .toList() : List.of();
        return new RespostaUsuarioDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getApelido(),
            orgs,
            projs
        );

    }

}
