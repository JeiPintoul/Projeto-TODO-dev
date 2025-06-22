package com.tododev.backend.dto;

import com.tododev.backend.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CriarUsuarioDTO(

    @NotNull(message = "Nome é obrigatório")
    String nome,
    @NotNull(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    String email,
    @NotNull(message = "Senha é obrigatória")
    @jakarta.validation.constraints.Size(min = 6,
                        message = "Senha deve ter pelo menos 6 caracteres")
    String senha
) {

    public static CriarUsuarioDTO criarDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new CriarUsuarioDTO(
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getSenha()
        );
    }

    public Usuario paraEntidade() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        return usuario;
    }
}
