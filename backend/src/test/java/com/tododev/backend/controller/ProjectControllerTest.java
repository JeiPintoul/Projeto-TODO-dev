package com.tododev.backend.controller;

import com.tododev.backend.model.Projeto;
import com.tododev.backend.service.ProjetoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjetoController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjetoService projetoService;

    private Projeto projetoAmostra;

    @BeforeEach
    public void setup() {
        projetoAmostra = new Projeto();
        projetoAmostra.setId(1L);
        projetoAmostra.setNome("Projeto Amostra");
        projetoAmostra.setDescricao("Descrição do Projeto Amostra");
    }

    @Test
    public void testCriarProjeto() throws Exception {
        Mockito.when(projetoService.criarProjeto(anyLong(), anyLong(), anyString(), anyString()))
                .thenReturn(projetoAmostra);

        mockMvc.perform(post("/api/projetos")
                .param("organizacaoId", "1")
                .param("gerenteId", "1")
                .param("nome", "Projeto Amostra")
                .param("descricao", "Descrição do Projeto Amostra")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projetoAmostra.getId()))
                .andExpect(jsonPath("$.nome").value(projetoAmostra.getNome()))
                .andExpect(jsonPath("$.descricao").value(projetoAmostra.getDescricao()));
    }

    @Test
    public void testGetProjetosPorOrganizacao() throws Exception {
        Mockito.when(projetoService.getProjetosPorOrganizacao(anyLong()))
                .thenReturn(List.of(projetoAmostra));

        mockMvc.perform(get("/api/projetos/organizacao/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(projetoAmostra.getId()))
                .andExpect(jsonPath("$[0].nome").value(projetoAmostra.getNome()));
    }

    @Test
    public void testGetProjetoPorIdEncontrado() throws Exception {
        Mockito.when(projetoService.getProjetoPorId(eq(1L)))
                .thenReturn(Optional.of(projetoAmostra));

        mockMvc.perform(get("/api/projetos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projetoAmostra.getId()))
                .andExpect(jsonPath("$.nome").value(projetoAmostra.getNome()));
    }

    @Test
    public void testGetProjectByIdNotFound() throws Exception {
        Mockito.when(projetoService.getProjetoPorId(eq(2L)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/projetos/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
