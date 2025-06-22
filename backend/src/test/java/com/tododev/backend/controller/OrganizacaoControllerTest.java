package com.tododev.backend.controller;

import com.tododev.backend.dto.OrganizacaoResumoDTO;
import com.tododev.backend.dto.UsuarioRespostaDTO;
import com.tododev.backend.dto.ProjetoResumoDTO;
import com.tododev.backend.service.OrganizacaoService;
import com.tododev.backend.service.UsuarioOrganizacaoService;
import com.tododev.backend.service.ConviteOrganizacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrganizacaoController.class)
class OrganizacaoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrganizacaoService organizacaoService;
    @MockBean
    private UsuarioOrganizacaoService usuarioOrganizacaoService;
    @MockBean
    private ConviteOrganizacaoService conviteOrganizacaoService;

    @BeforeEach
    void setUp() {}

    @Test
    void listarOrganizacoesPorUsuario_deveRetornar200() throws Exception {
        com.tododev.backend.model.Organizacao orgEntity = new com.tododev.backend.model.Organizacao();
        orgEntity.setId(1L);
        orgEntity.setNome("Org Teste");
        orgEntity.setDescricao("Descricao");
        when(organizacaoService.listarOrganizacoes(1L)).thenReturn(List.of(orgEntity));
        mockMvc.perform(get("/api/organizacoes/usuarios/1/organizacoes?usuarioId=1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Org Teste"))
                .andExpect(jsonPath("$[0].descricao").value("Descricao"));
    }

    @Test
    void listarUsuarios_deveRetornar200() throws Exception {
        UsuarioRespostaDTO usuario = new UsuarioRespostaDTO("1", "Ana", "12345678901", "999999999", "ana@email.com", "senha", true);
        when(usuarioOrganizacaoService.listarTodosUsuarios()).thenReturn(List.of(usuario));
        mockMvc.perform(get("/api/organizacoes/usuarios")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Ana"));
    }

    @Test
    void listarUsuarios_comBusca_deveRetornar200() throws Exception {
        UsuarioRespostaDTO usuario = new UsuarioRespostaDTO("2", "Bia", "12345678902", "988888888", "bia@email.com", "senha", false);
        when(usuarioOrganizacaoService.buscarUsuariosPorTermo("ana")).thenReturn(List.of(usuario));
        mockMvc.perform(get("/api/organizacoes/usuarios?termo=ana")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("2"))
                .andExpect(jsonPath("$[0].name").value("Bia"));
    }

    @Test
    void listarProjetosPorOrganizacao_deveRetornar200() throws Exception {
        ProjetoResumoDTO projeto = new ProjetoResumoDTO(1L, "Projeto Teste");
        when(organizacaoService.listarProjetosPorOrganizacao(1L)).thenReturn(List.of(projeto));
        mockMvc.perform(get("/api/organizacoes/1/projetos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Projeto Teste"));
    }

    @Test
    void listarProjetosPorOrganizacao_comBusca_deveRetornar200() throws Exception {
        ProjetoResumoDTO projeto = new ProjetoResumoDTO(2L, "Projeto Dev");
        when(organizacaoService.buscarProjetosPorOrganizacaoETermo(1L, "dev")).thenReturn(List.of(projeto));
        mockMvc.perform(get("/api/organizacoes/1/projetos?termo=dev")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].nome").value("Projeto Dev"));
    }
}
