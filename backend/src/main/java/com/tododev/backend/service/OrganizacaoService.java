package com.tododev.backend.service;

import com.tododev.backend.model.Organizacao;
import com.tododev.backend.repository.OrganizacaoRepository;
import com.tododev.backend.dto.ProjetoResumoDTO;
import com.tododev.backend.repository.ProjetoRepository;
import com.tododev.backend.model.Usuario;
import com.tododev.backend.model.UsuarioOrganizacao;
import com.tododev.backend.model.Funcao;
import com.tododev.backend.repository.UsuarioRepository;
import com.tododev.backend.repository.UsuarioOrganizacaoRepository;

import jakarta.transaction.Transactional;

import com.tododev.backend.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class OrganizacaoService {
    private final OrganizacaoRepository organizacaoRepository;
    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioOrganizacaoRepository usuarioOrganizacaoRepository;

    public Organizacao criarOrganizacao(String nome, String descricao, Long usuarioId) {
        if (organizacaoRepository.findAll().stream().anyMatch(o -> o.getNome().equalsIgnoreCase(nome))) {
            throw new IllegalArgumentException("Já existe uma organização com este nome.");
        }
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        Organizacao org = new Organizacao();
        org.setNome(nome);
        org.setDescricao(descricao);
        Organizacao savedOrg = organizacaoRepository.save(org);

        UsuarioOrganizacao uo = new UsuarioOrganizacao();
        uo.setUsuario(usuario);
        uo.setOrganizacao(savedOrg);
        uo.setFuncao(Funcao.GERENTE);
        usuarioOrganizacaoRepository.save(uo);

        return savedOrg;
    }

    public List<Organizacao> listarOrganizacoes(Long usuarioId) {
        // Retorna apenas organizações que o usuário participa
        return organizacaoRepository.findAll().stream()
            .filter(org -> org.getUsuariosOrganizacao().stream().anyMatch(uo -> uo.getUsuario().getId().equals(usuarioId)))
            .toList();
    }

    public Organizacao getOrganizacaoPorId(Long id, Long usuarioId) {
        Organizacao org = organizacaoRepository.findById(id).orElseThrow(() -> 
            new RecursoNaoEncontradoException("Organização não encontrada com o ID: " + id));
        boolean isMembro = org.getUsuariosOrganizacao().stream().anyMatch(uo -> uo.getUsuario().getId().equals(usuarioId));
        if (!isMembro) {
            throw new IllegalStateException("Usuário não faz parte da organização.");
        }
        return org;
    }

    public List<ProjetoResumoDTO> listarProjetosPorOrganizacao(Long organizacaoId) {
        return projetoRepository.findByOrganizacaoId(organizacaoId).stream()
            .map(p -> new ProjetoResumoDTO(p.getId(), p.getNome()))
            .toList();
    }

    public List<ProjetoResumoDTO> buscarProjetosPorOrganizacaoETermo(Long organizacaoId, String termo) {
        String termoLower = termo.toLowerCase();
        return projetoRepository.findByOrganizacaoId(organizacaoId).stream()
            .filter(p -> p.getNome().toLowerCase().contains(termoLower)
                || (p.getDescricao() != null && p.getDescricao().toLowerCase().contains(termoLower)))
            .map(p -> new ProjetoResumoDTO(p.getId(), p.getNome()))
            .toList();
    }
}
