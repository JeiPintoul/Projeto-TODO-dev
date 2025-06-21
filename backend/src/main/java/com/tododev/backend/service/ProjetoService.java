package com.tododev.backend.service;

import com.tododev.backend.exception.RecursoNaoEncontradoException; // Importe a nova exceção
import com.tododev.backend.model.*;
import com.tododev.backend.repository.*;

import jakarta.transaction.Transactional;

import com.tododev.backend.dto.AtualizarProjetoDTO;
import com.tododev.backend.dto.AdicionarMembroProjetoDTO;
import com.tododev.backend.dto.AdicionarArtefatoProjetoDTO;
import com.tododev.backend.dto.ProjetoResumoDTO;
import com.tododev.backend.dto.CriarProjetoDTO;
import com.tododev.backend.dto.ProjetoRespostaDTO;

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

    /**
     * Converte uma entidade Projeto para o DTO de resposta, garantindo compatibilidade total com o frontend.
     */
    private ProjetoRespostaDTO toProjetoRespostaDTO(Projeto projeto) {
        return ProjetoRespostaDTO.fromEntity(projeto);
    }

    /**
     * Cria um novo projeto e retorna o DTO de resposta compatível com o frontend.
     */
    public ProjetoRespostaDTO criarProjeto(CriarProjetoDTO dto, Long usuarioId) {
        // Extrai IDs das listas de objetos
        List<Long> companyIds = dto.companies().stream().map(c -> Long.parseLong(c.id())).toList();
        List<Long> managerIds = dto.managers().stream().map(m -> Long.parseLong(m.id())).toList();
        List<Long> workerIds = dto.workers().stream().map(w -> Long.parseLong(w.id())).toList();
        // Validação: o usuário que está criando deve estar na lista de managers
        if (managerIds == null || !managerIds.contains(usuarioId)) {
            throw new IllegalStateException("Usuário não está na lista de gerentes do projeto.");
        }
        Projeto projeto = new Projeto();
        projeto.setNome(dto.nome());
        projeto.setDescricao(dto.descricao());
        projeto.setCor(dto.cor());
        projeto.setStatus(dto.status());
        projeto.setArtefatos(dto.artefatos());
        projeto.setDataCriacao(LocalDateTime.now());
        // Conversão de datas
        if (dto.dataInicio() != null && !dto.dataInicio().isBlank()) {
            projeto.setDataInicio(LocalDateTime.parse(dto.dataInicio()));
        }
        if (dto.dataVencimento() != null && !dto.dataVencimento().isBlank()) {
            projeto.setDataVencimento(LocalDateTime.parse(dto.dataVencimento()));
        }
        // Associações com organizações
        List<Organizacao> organizacoes = organizacaoRepository.findAllById(companyIds);
        projeto.setOrganizacoes(organizacoes);
        Projeto projetoSalvo = projetoRepository.save(projeto);
        // Associações com usuários (gerentes e trabalhadores)
        for (Long managerId : managerIds) {
            Usuario gerente = usuarioRepository.findById(managerId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Gerente não encontrado com o ID: " + managerId));
            UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
            usuarioProjeto.setProjeto(projetoSalvo);
            usuarioProjeto.setUsuario(gerente);
            usuarioProjetoRepository.save(usuarioProjeto);
        }
        for (Long workerId : workerIds) {
            if (!managerIds.contains(workerId)) {
                Usuario trabalhador = usuarioRepository.findById(workerId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Trabalhador não encontrado com o ID: " + workerId));
                UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
                usuarioProjeto.setProjeto(projetoSalvo);
                usuarioProjeto.setUsuario(trabalhador);
                usuarioProjetoRepository.save(usuarioProjeto);
            }
        }
        return toProjetoRespostaDTO(projetoSalvo);
    }

    /**
     * Lista todos os projetos de uma organização, retornando DTOs compatíveis com o frontend.
     */
    public List<ProjetoRespostaDTO> getProjetosPorOrganizacao(Long organizacaoId, Long usuarioId) {
        UsuarioOrganizacao usuarioOrg = usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioId, organizacaoId);
        if (usuarioOrg == null) {
            throw new IllegalStateException("Usuário não faz parte da organização.");
        }
        return projetoRepository.findByOrganizacaoId(organizacaoId).stream()
            .map(this::toProjetoRespostaDTO)
            .toList();
    }

    /**
     * Busca um projeto por ID, retornando o DTO de resposta se encontrado e autorizado.
     */
    public Optional<ProjetoRespostaDTO> getProjetoPorId(Long projetoId, Long usuarioId) {
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_PROJETO_NAO_ENCONTRADO + projetoId));
        // Se o projeto estiver associado a organizações, o usuário deve ser membro de pelo menos uma delas
        if (!projeto.getOrganizacoes().isEmpty()) {
            boolean membro = projeto.getOrganizacoes().stream().anyMatch(org ->
                usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(usuarioId, org.getId()) != null
            );
            if (!membro) {
                return Optional.empty();
            }
        }
        return Optional.of(toProjetoRespostaDTO(projeto));
    }

    /**
     * Atualiza um projeto e retorna o DTO de resposta atualizado.
     */
    public ProjetoRespostaDTO atualizarProjeto(Long projetoId, Long usuarioId, AtualizarProjetoDTO dto) {
        // Extrai IDs das listas de objetos
        List<Long> companyIds = dto.companies().stream().map(c -> Long.parseLong(c.id())).toList();
        List<Long> managerIds = dto.managers().stream().map(m -> Long.parseLong(m.id())).toList();
        List<Long> workerIds = dto.workers().stream().map(w -> Long.parseLong(w.id())).toList();
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_PROJETO_NAO_ENCONTRADO + projetoId));
        // Só gerentes podem atualizar
        boolean isGerente = projeto.getUsuariosProjeto().stream()
            .anyMatch(up -> up.getUsuario().getId().equals(usuarioId) && managerIds.contains(usuarioId));
        if (!isGerente) {
            throw new IllegalStateException("Apenas um gerente pode atualizar o projeto.");
        }
        projeto.setNome(dto.nome());
        projeto.setDescricao(dto.descricao());
        projeto.setCor(dto.cor());
        projeto.setStatus(dto.status());
        projeto.setArtefatos(dto.artefatos());
        if (dto.dataInicio() != null && !dto.dataInicio().isBlank()) {
            projeto.setDataInicio(LocalDateTime.parse(dto.dataInicio()));
        }
        if (dto.dataVencimento() != null && !dto.dataVencimento().isBlank()) {
            projeto.setDataVencimento(LocalDateTime.parse(dto.dataVencimento()));
        }
        // Atualiza organizações
        List<Organizacao> novasOrgs = organizacaoRepository.findAllById(companyIds);
        projeto.setOrganizacoes(novasOrgs);
        // Atualiza membros (remove todos e adiciona novamente)
        usuarioProjetoRepository.deleteAll(projeto.getUsuariosProjeto());
        for (Long managerId : managerIds) {
            Usuario gerente = usuarioRepository.findById(managerId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Gerente não encontrado com o ID: " + managerId));
            UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
            usuarioProjeto.setProjeto(projeto);
            usuarioProjeto.setUsuario(gerente);
            usuarioProjetoRepository.save(usuarioProjeto);
        }
        for (Long workerId : workerIds) {
            if (!managerIds.contains(workerId)) {
                Usuario trabalhador = usuarioRepository.findById(workerId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Trabalhador não encontrado com o ID: " + workerId));
                UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
                usuarioProjeto.setProjeto(projeto);
                usuarioProjeto.setUsuario(trabalhador);
                usuarioProjetoRepository.save(usuarioProjeto);
            }
        }
        return toProjetoRespostaDTO(projetoRepository.save(projeto));
    }

    public void adicionarMembrosAoProjeto(Long projetoId, Long usuarioId, List<AdicionarMembroProjetoDTO> membros) {
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(MSG_PROJETO_NAO_ENCONTRADO + projetoId));
        boolean isGerente = projeto.getUsuariosProjeto().stream()
            .anyMatch(up -> up.getUsuario().getId().equals(usuarioId));
        if (!isGerente) {
            throw new IllegalStateException("Apenas gerente pode adicionar membros.");
        }
        for (AdicionarMembroProjetoDTO dto : membros) {
            Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + dto.usuarioId()));
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
        boolean isGerente = projeto.getUsuariosProjeto().stream()
            .anyMatch(up -> up.getUsuario().getId().equals(usuarioId));
        if (!isGerente) {
            throw new IllegalStateException("Apenas gerente pode deletar o projeto.");
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
        projeto.setCor(null);
        projeto.setStatus("PESSOAL");
        projeto.setDataCriacao(LocalDateTime.now());
        projeto.setOrganizacoes(new java.util.ArrayList<>());
        Projeto projetoSalvo = projetoRepository.save(projeto);
        UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
        usuarioProjeto.setProjeto(projetoSalvo);
        usuarioProjeto.setUsuario(usuario);
        usuarioProjetoRepository.save(usuarioProjeto);
        return projetoSalvo;
    }

    public List<ProjetoResumoDTO> listarProjetosPessoais(Long usuarioId) {
        return projetoRepository.findAll().stream()
            .filter(p -> p.getOrganizacoes().isEmpty() && p.getUsuariosProjeto().stream().anyMatch(up -> up.getUsuario().getId().equals(usuarioId)))
            .map(p -> new ProjetoResumoDTO(p.getId(), p.getNome()))
            .toList();
    }
}
