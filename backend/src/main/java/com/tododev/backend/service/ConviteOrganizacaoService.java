package com.tododev.backend.service;

import com.tododev.backend.dto.ConviteOrganizacaoDTO;
import com.tododev.backend.model.ConviteOrganizacao;
import com.tododev.backend.model.Funcao;
import com.tododev.backend.model.Organizacao;
import com.tododev.backend.model.Usuario;
import com.tododev.backend.repository.ConviteOrganizacaoRepository;
import com.tododev.backend.repository.OrganizacaoRepository;
import com.tododev.backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ConviteOrganizacaoService {
    private final ConviteOrganizacaoRepository conviteRepo;
    private final UsuarioRepository usuarioRepo;
    private final OrganizacaoRepository organizacaoRepo;

    public void convidarUsuario(Long organizacaoId, Long usuarioId, Funcao funcao) {
        if (conviteRepo.existsByUsuarioIdAndOrganizacaoIdAndStatus(usuarioId, organizacaoId, ConviteOrganizacao.StatusConvite.PENDENTE)) {
            throw new IllegalArgumentException("Já existe convite pendente para este usuário nesta organização.");
        }
        Usuario usuario = usuarioRepo.findById(usuarioId).orElseThrow();
        Organizacao org = organizacaoRepo.findById(organizacaoId).orElseThrow();
        ConviteOrganizacao convite = new ConviteOrganizacao();
        convite.setUsuario(usuario);
        convite.setOrganizacao(org);
        convite.setFuncao(funcao);
        convite.setStatus(ConviteOrganizacao.StatusConvite.PENDENTE);
        conviteRepo.save(convite);
    }

    public List<ConviteOrganizacaoDTO> listarConvitesPendentes(Long usuarioId) {
        return conviteRepo.findByUsuarioIdAndStatus(usuarioId, ConviteOrganizacao.StatusConvite.PENDENTE)
            .stream().map(c -> new ConviteOrganizacaoDTO(
                c.getId(),
                c.getUsuario().getId(),
                c.getOrganizacao().getId(),
                c.getOrganizacao().getNome(),
                c.getFuncao(),
                c.getStatus().name()
            )).toList();
    }

    public void aceitarConvite(Long conviteId, Long usuarioId) {
        ConviteOrganizacao convite = conviteRepo.findById(conviteId).orElseThrow();
        if (!convite.getUsuario().getId().equals(usuarioId)) throw new IllegalStateException("Convite não pertence ao usuário");
        convite.setStatus(ConviteOrganizacao.StatusConvite.ACEITO);
        conviteRepo.save(convite);
        // Aqui você pode adicionar o usuário à organização
    }

    public void recusarConvite(Long conviteId, Long usuarioId) {
        ConviteOrganizacao convite = conviteRepo.findById(conviteId).orElseThrow();
        if (!convite.getUsuario().getId().equals(usuarioId)) throw new IllegalStateException("Convite não pertence ao usuário");
        convite.setStatus(ConviteOrganizacao.StatusConvite.RECUSADO);
        conviteRepo.save(convite);
    }
}
