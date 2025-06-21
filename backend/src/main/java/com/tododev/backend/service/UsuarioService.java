package com.tododev.backend.service;

import com.tododev.backend.model.Usuario;
import com.tododev.backend.repository.UsuarioRepository;
import com.tododev.backend.dto.OrganizacaoResumoDTO;
import com.tododev.backend.model.UsuarioOrganizacao;
import com.tododev.backend.repository.UsuarioOrganizacaoRepository;

import jakarta.transaction.Transactional;

import com.tododev.backend.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioOrganizacaoRepository usuarioOrganizacaoRepository;

    public Usuario criarUsuario(String nome, String email, String senha, String apelido, String cpf, String telefone) {
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail.");
        }
        if (apelido != null && !apelido.isBlank() && usuarioRepository.existsByApelidoIgnoreCase(apelido)) {
            throw new IllegalArgumentException("Já existe um usuário com este apelido.");
        }
        if (cpf != null && usuarioRepository.findAll().stream().anyMatch(u -> cpf.equals(u.getCpf()))) {
            throw new IllegalArgumentException("Já existe um usuário com este CPF.");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setApelido(apelido);
        usuario.setCpf(cpf);
        usuario.setTelefone(telefone);
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Erro ao salvar usuário: dados duplicados ou inválidos.");
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> 
            new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + id));
    }

    public List<Usuario> buscarUsuarios(String termo) {
        String termoLower = termo.toLowerCase();
        return usuarioRepository.findAll().stream()
            .filter(u -> u.getNome().toLowerCase().contains(termoLower)
                || u.getEmail().toLowerCase().contains(termoLower)
                || (u.getApelido() != null && u.getApelido().toLowerCase().contains(termoLower)))
            .toList();
    }

    public Usuario atualizarUsuario(Long id, String nome, String email, String senha, String apelido) {
        Usuario usuario = getUsuarioPorId(id);
        if (nome != null) usuario.setNome(nome);
        if (email != null) usuario.setEmail(email);
        if (senha != null) usuario.setSenha(senha);
        if (apelido != null) usuario.setApelido(apelido);
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Erro ao atualizar usuário: dados duplicados ou inválidos.");
        }
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = getUsuarioPorId(id);
        usuarioRepository.delete(usuario);
    }

    public List<OrganizacaoResumoDTO> listarOrganizacoesPorUsuario(Long usuarioId) {
        List<UsuarioOrganizacao> usuarioOrgs = usuarioOrganizacaoRepository.findByUsuarioId(usuarioId);
        return usuarioOrgs.stream()
            .map(uo -> new OrganizacaoResumoDTO(
                uo.getOrganizacao().getId(),
                uo.getOrganizacao().getNome(),
                uo.getOrganizacao().getDescricao()
            ))
            .collect(Collectors.toList());
    }
}
