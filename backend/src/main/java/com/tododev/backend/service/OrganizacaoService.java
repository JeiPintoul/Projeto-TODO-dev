package com.tododev.backend.service;

import com.tododev.backend.model.Organizacao;
import com.tododev.backend.repository.OrganizacaoRepository;

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

    public Organizacao criarOrganizacao(String nome, String descricao) {
        if (organizacaoRepository.findAll().stream().anyMatch(o -> o.getNome().equalsIgnoreCase(nome))) {
            throw new IllegalArgumentException("Já existe uma organização com este nome.");
        }
        Organizacao org = new Organizacao();
        org.setNome(nome);
        org.setDescricao(descricao);
        return organizacaoRepository.save(org);
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
}
