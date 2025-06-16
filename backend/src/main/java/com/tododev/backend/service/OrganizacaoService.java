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

    public List<Organizacao> listarOrganizacoes() {
        return organizacaoRepository.findAll();
    }

    public Organizacao getOrganizacaoPorId(Long id) {
        return organizacaoRepository.findById(id).orElseThrow(() -> 
            new RecursoNaoEncontradoException("Organização não encontrada com o ID: " + id));
    }
}
