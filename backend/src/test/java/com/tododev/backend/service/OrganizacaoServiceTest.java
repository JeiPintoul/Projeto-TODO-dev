package com.tododev.backend.service;

import com.tododev.backend.model.Organizacao;
import com.tododev.backend.model.Usuario;
import com.tododev.backend.model.UsuarioOrganizacao;
import com.tododev.backend.repository.OrganizacaoRepository;
import com.tododev.backend.repository.UsuarioRepository;
import com.tododev.backend.repository.UsuarioOrganizacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizacaoServiceTest {
    @Mock
    private OrganizacaoRepository organizacaoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioOrganizacaoRepository usuarioOrganizacaoRepository;
    @InjectMocks
    private OrganizacaoService organizacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarOrganizacao_sucesso() {
        Organizacao org = new Organizacao();
        org.setId(1L);
        org.setNome("Org1");
        org.setDescricao("Desc");
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        when(organizacaoRepository.findAll()).thenReturn(new ArrayList<>());
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));
        when(organizacaoRepository.save(any(Organizacao.class))).thenReturn(org);
        when(usuarioOrganizacaoRepository.save(any())).thenReturn(null);
        Organizacao salvo = organizacaoService.criarOrganizacao("Org1", "Desc", 2L);
        assertEquals("Org1", salvo.getNome());
    }

    @Test
    void criarOrganizacao_nomeDuplicado() {
        Organizacao org = new Organizacao();
        org.setNome("Org1");
        when(organizacaoRepository.findAll()).thenReturn(List.of(org));
        assertThrows(IllegalArgumentException.class, () ->
            organizacaoService.criarOrganizacao("Org1", "Desc", 1L)
        );
    }

    @Test
    void listarOrganizacoes_usuarioParticipa() {
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        Organizacao org = new Organizacao();
        org.setId(1L);
        org.setNome("Org1");
        UsuarioOrganizacao uo = new UsuarioOrganizacao();
        uo.setUsuario(usuario);
        List<UsuarioOrganizacao> listaUO = List.of(uo);
        org.setUsuariosOrganizacao(listaUO);
        when(organizacaoRepository.findAll()).thenReturn(List.of(org));
        List<Organizacao> result = organizacaoService.listarOrganizacoes(10L);
        assertEquals(1, result.size());
    }

    @Test
    void getOrganizacaoPorId_usuarioNaoMembro() {
        Organizacao org = new Organizacao();
        org.setId(1L);
        org.setUsuariosOrganizacao(new ArrayList<>());
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.of(org));
        assertThrows(IllegalStateException.class, () -> organizacaoService.getOrganizacaoPorId(1L, 99L));
    }

    @Test
    void getOrganizacaoPorId_usuarioMembro() {
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        Organizacao org = new Organizacao();
        org.setId(1L);
        UsuarioOrganizacao uo = new UsuarioOrganizacao();
        uo.setUsuario(usuario);
        org.setUsuariosOrganizacao(List.of(uo));
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.of(org));
        Organizacao result = organizacaoService.getOrganizacaoPorId(1L, 10L);
        assertEquals(1L, result.getId());
    }
}
