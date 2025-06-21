package com.tododev.backend.dto;

import com.tododev.backend.model.Usuario;

public record UsuarioRespostaDTO(
    String id,
    String name,
    String cpf,
    String phone,
    String email,
    String password,
    Boolean isManager
) {
    public static UsuarioRespostaDTO fromEntity(Usuario usuario, boolean isManager) {
        return new UsuarioRespostaDTO(
            usuario.getId() != null ? usuario.getId().toString() : null,
            usuario.getNome(),
            usuario.getCpf(),
            usuario.getTelefone(),
            usuario.getEmail(),
            usuario.getSenha(),
            isManager
        );
    }
}
