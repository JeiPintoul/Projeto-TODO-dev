package com.tododev.backend.service;

import com.tododev.backend.exception.RecursoNaoEncontradoException;
import com.tododev.backend.model.Usuario;
import com.tododev.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarUsuario_sucesso() {
        when(usuarioRepository.existsByEmailIgnoreCase("email@teste.com")).thenReturn(false);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Nome");
        usuario.setEmail("email@teste.com");
        usuario.setSenha("senha123");
        usuario.setCpf("12345678901");
        usuario.setTelefone("11999999999");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        Usuario salvo = usuarioService.criarUsuario("Nome", "email@teste.com", "senha123", "12345678901", "11999999999");
        assertEquals("Nome", salvo.getNome());
        assertEquals("email@teste.com", salvo.getEmail());
        assertEquals("12345678901", salvo.getCpf());
        assertEquals("11999999999", salvo.getTelefone());
    }

    @Test
    void criarUsuario_emailDuplicado() {
        when(usuarioRepository.existsByEmailIgnoreCase("email@teste.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () ->
            usuarioService.criarUsuario("Nome", "email@teste.com", "senha123", "12345678901", "11999999999")
        );
    }

    @Test
    void criarUsuario_cpfDuplicado() {
        when(usuarioRepository.existsByEmailIgnoreCase("email2@teste.com")).thenReturn(false);
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setCpf("12345678901");
        when(usuarioRepository.findAll()).thenReturn(java.util.List.of(usuarioExistente));
        assertThrows(IllegalArgumentException.class, () ->
            usuarioService.criarUsuario("Nome", "email2@teste.com", "senha123", "12345678901", "11999999999")
        );
    }

    @Test
    void getUsuarioPorId_naoEncontrado() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> usuarioService.getUsuarioPorId(99L));
    }

    @Test
    void atualizarUsuario_sucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Nome");
        usuario.setEmail("email@teste.com");
        usuario.setSenha("senha123");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        Usuario atualizado = usuarioService.atualizarUsuario(1L, "NovoNome", null, null);
        assertEquals("NovoNome", atualizado.getNome());
    }

    @Test
    void deletarUsuario_sucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Nome");
        usuario.setEmail("email@teste.com");
        usuario.setSenha("senha123");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);
        assertDoesNotThrow(() -> usuarioService.deletarUsuario(1L));
    }
}
