package com.tododev.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tododev.backend.dto.OrganizacaoResumoDTO;
import com.tododev.backend.service.OrganizacaoService;
import com.tododev.backend.service.UsuarioOrganizacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {}

    @Test
    void listarOrganizacoesPorUsuario_deveRetornar200() throws Exception {
        OrganizacaoResumoDTO org = new OrganizacaoResumoDTO(1L, "Org Teste", "Descricao");
        when(organizacaoService.listarOrganizacoes(1L)).thenReturn(List.of());
        mockMvc.perform(get("/api/organizacoes/usuarios/1/organizacoes?usuarioId=1"))
                .andExpect(status().isOk());
    }
}
