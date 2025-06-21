package com.tododev.backend.service;

import com.tododev.backend.dto.AdicionarUsuarioOrganizacaoDTO;
import com.tododev.backend.dto.UsuarioOrganizacaoRespostaDTO;
import com.tododev.backend.dto.RespostaUsuarioDTO;
import com.tododev.backend.model.Funcao;
import com.tododev.backend.model.Organizacao;
import com.tododev.backend.model.Usuario;
import com.tododev.backend.model.UsuarioOrganizacao;
import com.tododev.backend.repository.OrganizacaoRepository;
import com.tododev.backend.repository.UsuarioOrganizacaoRepository;
import com.tododev.backend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

import com.tododev.backend.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UsuarioOrganizacaoService {
    private final UsuarioOrganizacaoRepository usuarioOrganizacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final OrganizacaoRepository organizacaoRepository;

    public UsuarioOrganizacaoRespostaDTO adicionarUsuario(Long organizacaoId, AdicionarUsuarioOrganizacaoDTO dto) {
        Organizacao org = organizacaoRepository.findById(organizacaoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Organização não encontrada com o ID: " + organizacaoId));
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + dto.usuarioId()));
        if (usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuario.getId(), org.getId()) != null) {
            throw new IllegalArgumentException("Usuário já faz parte da organização.");
        }
        UsuarioOrganizacao uo = new UsuarioOrganizacao();
        uo.setUsuario(usuario);
        uo.setOrganizacao(org);
        uo.setFuncao(dto.funcao());
        usuarioOrganizacaoRepository.save(uo);
        return new UsuarioOrganizacaoRespostaDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getApelido(), dto.funcao());
    }

    public List<UsuarioOrganizacaoRespostaDTO> listarUsuariosPorOrganizacao(Long organizacaoId) {
        return usuarioOrganizacaoRepository.findByOrganizacaoId(organizacaoId).stream()
            .map(uo -> new UsuarioOrganizacaoRespostaDTO(
                uo.getUsuario().getId(),
                uo.getUsuario().getNome(),
                uo.getUsuario().getEmail(),
                uo.getUsuario().getApelido(),
                uo.getFuncao()
            )).toList();
    }

    public List<UsuarioOrganizacaoRespostaDTO> listarUsuariosPorOrganizacao(Long organizacaoId, Long usuarioId) {
        // Só membros podem listar
        UsuarioOrganizacao membro = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioId, organizacaoId);
        if (membro == null) {
            throw new IllegalStateException("Usuário não faz parte da organização.");
        }
        return listarUsuariosPorOrganizacao(organizacaoId);
    }

    public UsuarioOrganizacaoRespostaDTO adicionarUsuario(Long organizacaoId, Long usuarioId, AdicionarUsuarioOrganizacaoDTO dto) {
        // Só gerente pode adicionar
        UsuarioOrganizacao gerente = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioId, organizacaoId);
        if (gerente == null || gerente.getFuncao() != Funcao.GERENTE) {
            throw new IllegalStateException("Apenas gerente pode adicionar usuário.");
        }
        return adicionarUsuario(organizacaoId, dto);
    }

    public void alterarFuncaoUsuario(Long organizacaoId, Long usuarioId, Funcao novaFuncao, Long usuarioIdLogado) {
        // Só gerente pode alterar função
        UsuarioOrganizacao gerente = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioIdLogado, organizacaoId);
        if (gerente == null || gerente.getFuncao() != Funcao.GERENTE) {
            throw new IllegalStateException("Apenas gerente pode alterar função.");
        }
        alterarFuncaoUsuario(organizacaoId, usuarioId, novaFuncao);
    }

    public void removerUsuario(Long organizacaoId, Long usuarioId, Long usuarioIdLogado) {
        // Só gerente pode remover
        UsuarioOrganizacao gerente = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioIdLogado, organizacaoId);
        if (gerente == null || gerente.getFuncao() != Funcao.GERENTE) {
            throw new IllegalStateException("Apenas gerente pode remover usuário.");
        }
        removerUsuario(organizacaoId, usuarioId);
    }

    public void alterarFuncaoUsuario(Long organizacaoId, Long usuarioId, Funcao novaFuncao) {
        UsuarioOrganizacao uo = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioId, organizacaoId);
        if (uo == null) {
            throw new RecursoNaoEncontradoException("Usuário não faz parte da organização.");
        }
        uo.setFuncao(novaFuncao);
        usuarioOrganizacaoRepository.save(uo);
    }

    public void removerUsuario(Long organizacaoId, Long usuarioId) {
        UsuarioOrganizacao uo = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioId, organizacaoId);
        if (uo == null) {
            throw new RecursoNaoEncontradoException("Usuário não faz parte da organização.");
        }
        usuarioOrganizacaoRepository.delete(uo);
    }

    public List<RespostaUsuarioDTO> listarTodosUsuarios() {
        return usuarioRepository.findAll().stream()
            .map(u -> new RespostaUsuarioDTO(u.getId(), u.getNome(), u.getEmail(), u.getApelido(), List.of(), List.of()))
            .toList();
    }

    public List<RespostaUsuarioDTO> buscarUsuariosPorTermo(String termo) {
        String termoLower = termo.toLowerCase();
        return usuarioRepository.findAll().stream()
            .filter(u -> u.getNome().toLowerCase().contains(termoLower)
                || u.getEmail().toLowerCase().contains(termoLower)
                || (u.getApelido() != null && u.getApelido().toLowerCase().contains(termoLower)))
            .map(u -> new RespostaUsuarioDTO(u.getId(), u.getNome(), u.getEmail(), u.getApelido(), List.of(), List.of()))
            .toList();
    }

    public void validarGerente(Long organizacaoId, Long usuarioId) {
        var gerente = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioId, organizacaoId);
        if (gerente == null || gerente.getFuncao() != Funcao.GERENTE) {
            throw new IllegalStateException("Apenas gerente pode realizar esta ação.");
        }
    }
}
