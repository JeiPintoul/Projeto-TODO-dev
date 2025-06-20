package com.tododev.backend.service;

import com.tododev.backend.exception.RecursoNaoEncontradoException; // Importe a nova exceção
import com.tododev.backend.model.*;
import com.tododev.backend.repository.*;

import jakarta.transaction.Transactional;

import com.tododev.backend.dto.AtualizarProjetoDTO;
import com.tododev.backend.dto.AdicionarMembroProjetoDTO;
import com.tododev.backend.dto.AdicionarArtefatoProjetoDTO;
import com.tododev.backend.dto.ProjetoResumoDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioProjetoRepository usuarioProjetoRepository;
    private final ArtefatoProjetoRepository artefatoProjetoRepository;
    private final UsuarioOrganizacaoRepository usuarioOrganizacaoRepository; // Adicionado repositório

        public static final String MSG_PROJETO_NAO_ENCONTRADO = "Projeto não encontrada com o ID: ";

    public Projeto criarProjeto(Long organizacaoId, Long managerId, String nome, String descricao) {
        Organizacao organizacao = organizacaoRepository.findById(organizacaoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Organização não encontrada com o ID: " + organizacaoId));
        Usuario gerente = usuarioRepository.findById(managerId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Gerente não encontrado com o ID: " + managerId));
        UsuarioOrganizacao usuarioOrg = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(gerente.getId(), organizacao.getId());
        if (usuarioOrg == null || usuarioOrg.getFuncao() != Funcao.GERENTE) {
            throw new IllegalStateException("O usuário informado não é gerente da organização.");
        }
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
        usuarioProjetoRepository.save(usuarioProjeto);
        return projetoSalvo;
    }

    public List<Projeto> getProjetosPorOrganizacao(Long organizacaoId, Long usuarioId) {
        // Regra: só membros da organização podem listar projetos
        UsuarioOrganizacao usuarioOrg = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioId, organizacaoId);
        if (usuarioOrg == null) {
            throw new IllegalStateException("Usuário não faz parte da organização.");
        }
        return projetoRepository.findByOrganizacaoId(organizacaoId);
    }

    public Optional<Projeto> getProjetoPorId(Long projetoId, Long usuarioId) {
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_PROJETO_NAO_ENCONTRADO + projetoId));
        if (projeto.getOrganizacao() != null) {
            UsuarioOrganizacao usuarioOrg = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioId, projeto.getOrganizacao().getId());
            if (usuarioOrg == null) {
                return Optional.empty();
            }
        }
        return Optional.of(projeto);
    }

    public Projeto atualizarProjeto(Long projetoId, Long usuarioId, AtualizarProjetoDTO dto) {
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_PROJETO_NAO_ENCONTRADO + projetoId));
        if (!projeto.getGerente().getId().equals(usuarioId)) {
            throw new IllegalStateException("Apenas o gerente pode atualizar o projeto.");
        }
        projeto.setNome(dto.nome());
        projeto.setDescricao(dto.descricao());
        projeto.setStatus(StatusProjeto.valueOf(dto.status()));
        projeto.setDataTermino(dto.dataTermino());
        return projetoRepository.save(projeto);
    }

    public void adicionarMembrosAoProjeto(Long projetoId, Long usuarioId, List<AdicionarMembroProjetoDTO> membros) {
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_PROJETO_NAO_ENCONTRADO + projetoId));
        if (!projeto.getGerente().getId().equals(usuarioId)) {
            throw new IllegalStateException("Apenas o gerente pode adicionar membros.");
        }
        for (AdicionarMembroProjetoDTO dto : membros) {
            Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + dto.usuarioId()));
            UsuarioOrganizacao usuarioOrg = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuario.getId(), projeto.getOrganizacao().getId());
            if (usuarioOrg == null) {
                throw new IllegalStateException("Usuário não faz parte da organização do projeto.");
            }
            UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
            usuarioProjeto.setProjeto(projeto);
            usuarioProjeto.setUsuario(usuario);
            usuarioProjetoRepository.save(usuarioProjeto);
        }
    }

    public void adicionarArtefatoAoProjeto(Long projetoId, Long usuarioId, AdicionarArtefatoProjetoDTO dto) {
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_PROJETO_NAO_ENCONTRADO + projetoId));
        boolean isMembro = projeto.getUsuariosProjeto().stream().anyMatch(up -> up.getUsuario().getId().equals(usuarioId));
        if (!isMembro) {
            throw new IllegalStateException("Apenas membros do projeto podem adicionar artefatos.");
        }
        ArtefatoProjeto artefato = new ArtefatoProjeto();
        artefato.setProjeto(projeto);
        artefato.setConteudo(dto.conteudo());
        artefato.setTipo(dto.tipo());
        artefato.setEditado(java.time.LocalDateTime.now());
        artefatoProjetoRepository.save(artefato);
    }

    public void deletarProjeto(Long projetoId, Long usuarioId) {
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_PROJETO_NAO_ENCONTRADO + projetoId));
        if (!projeto.getGerente().getId().equals(usuarioId)) {
            throw new IllegalStateException("Apenas o gerente pode deletar o projeto.");
        }
        projetoRepository.delete(projeto);
    }

    public List<ProjetoResumoDTO> getProjetosPorUsuario(Long usuarioId) {
        return projetoRepository.findAll().stream()
            .filter(p -> p.getUsuariosProjeto().stream().anyMatch(up -> up.getUsuario().getId().equals(usuarioId)))
            .map(p -> new ProjetoResumoDTO(p.getId(), p.getNome()))
            .toList();
    }

    public Projeto criarProjetoPessoal(Long usuarioId, String nome, String descricao) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + usuarioId));
        Projeto projeto = new Projeto();
        projeto.setNome(nome);
        projeto.setDescricao(descricao);
        projeto.setOrganizacao(null);
        projeto.setGerente(usuario);
        projeto.setStatus(StatusProjeto.PENDENTE);
        projeto.setDataCriacao(LocalDateTime.now());
        Projeto projetoSalvo = projetoRepository.save(projeto);
        UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
        usuarioProjeto.setProjeto(projetoSalvo);
        usuarioProjeto.setUsuario(usuario);
        usuarioProjetoRepository.save(usuarioProjeto);
        return projetoSalvo;
    }

    public List<ProjetoResumoDTO> listarProjetosPessoais(Long usuarioId) {
        return projetoRepository.findAll().stream()
            .filter(p -> p.getOrganizacao() == null && p.getGerente() != null && p.getGerente().getId().equals(usuarioId))
            .map(p -> new ProjetoResumoDTO(p.getId(), p.getNome()))
            .toList();
    }
}
