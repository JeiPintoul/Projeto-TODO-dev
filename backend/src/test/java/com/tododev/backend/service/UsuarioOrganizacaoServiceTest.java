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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioOrganizacaoServiceTest {
    @Mock
    private UsuarioOrganizacaoRepository usuarioOrganizacaoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private OrganizacaoRepository organizacaoRepository;
    @InjectMocks
    private UsuarioOrganizacaoService usuarioOrganizacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarUsuariosPorOrganizacao_usuarioMembro() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        UsuarioOrganizacao uo = new UsuarioOrganizacao();
        uo.setUsuario(usuario);
        when(usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(1L, 2L)).thenReturn(uo);
        when(usuarioOrganizacaoRepository.findByOrganizacaoId(2L)).thenReturn(List.of(uo));
        List<UsuarioOrganizacaoRespostaDTO> result = usuarioOrganizacaoService.listarUsuariosPorOrganizacao(2L, 1L);
        assertEquals(1, result.size());
    }

    @Test
    void listarUsuariosPorOrganizacao_usuarioNaoMembro() {
        when(usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(1L, 2L)).thenReturn(null);
        assertThrows(IllegalStateException.class, () -> usuarioOrganizacaoService.listarUsuariosPorOrganizacao(2L, 1L));
    }

    @Test
    void adicionarUsuario_apenasGerente() {
        UsuarioOrganizacao gerente = new UsuarioOrganizacao();
        gerente.setFuncao(Funcao.GERENTE);
        when(usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(1L, 2L)).thenReturn(gerente);
        Organizacao org = new Organizacao();
        org.setId(2L);
        Usuario usuario = new Usuario();
        usuario.setId(3L);
        AdicionarUsuarioOrganizacaoDTO dto = new AdicionarUsuarioOrganizacaoDTO(3L, Funcao.DESENVOLVEDOR);
        when(organizacaoRepository.findById(2L)).thenReturn(Optional.of(org));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario));
        when(usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(3L, 2L)).thenReturn(null);
        UsuarioOrganizacaoRespostaDTO resp = usuarioOrganizacaoService.adicionarUsuario(2L, 1L, dto);
        assertEquals(3L, resp.usuarioId());
    }

    @Test
    void adicionarUsuario_naoGerente() {
        UsuarioOrganizacao membro = new UsuarioOrganizacao();
        membro.setFuncao(Funcao.DESENVOLVEDOR);
        when(usuarioOrganizacaoRepository.findByUsuarioIdAndOrganizacaoId(1L, 2L)).thenReturn(membro);
        AdicionarUsuarioOrganizacaoDTO dto = new AdicionarUsuarioOrganizacaoDTO(3L, Funcao.DESENVOLVEDOR);
        assertThrows(IllegalStateException.class, () -> usuarioOrganizacaoService.adicionarUsuario(2L, 1L, dto));
    }
}
