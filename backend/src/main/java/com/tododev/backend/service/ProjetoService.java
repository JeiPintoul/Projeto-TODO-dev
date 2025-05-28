package com.tododev.backend.service;

import com.tododev.backend.exception.RecursoNaoEncontradoException; // Importe a nova exceção
import com.tododev.backend.model.*;
import com.tododev.backend.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioProjetoRepository usuarioProjetoRepository;

    public Projeto criarProjeto(Long organizacaoId, Long managerId, String nome, String descricao) {

        Organizacao organizacao = organizacaoRepository.findById(organizacaoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Organização não encontrada com o ID: " + organizacaoId));

        Usuario gerente = usuarioRepository.findById(managerId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Gerente não encontrado com o ID: " + managerId));

        Projeto projeto = new Projeto();
        projeto.setNome(nome);
        projeto.setDescricao(descricao);
        projeto.setOrganizacao(organizacao);
        projeto.setGerente(gerente);
        projeto.setStatus(StatusProjeto.PENDENTE);
        projeto.setDataCriacao(LocalDateTime.now());

        Projeto projetoSalvo = projetoRepository.save(projeto);

        UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
        usuarioProjeto.setProjeto(projetoSalvo);
        usuarioProjeto.setUsuario(gerente);
        usuarioProjeto.setFuncao(Funcao.GERENTE);
        usuarioProjetoRepository.save(usuarioProjeto);

        return projetoSalvo;
    }

    public List<Projeto> getProjetosPorOrganizacao(Long organizacaoId) {
        return projetoRepository.findByOrganizacaoId(organizacaoId);
    }

    public Optional<Projeto> getProjetoPorId(Long projectId) {
        return projetoRepository.findById(projectId);
    }
}
