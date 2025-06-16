package com.tododev.backend.service;

import com.tododev.backend.dto.AdicionarUsuarioOrganizacaoDTO;
import com.tododev.backend.dto.UsuarioOrganizacaoRespostaDTO;
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
import java.util.stream.Collectors;

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
            )).collect(Collectors.toList());
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
}
