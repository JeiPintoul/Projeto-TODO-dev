package com.tododev.backend.controller;

import com.tododev.backend.dto.ProjetoResumoDTO;
import com.tododev.backend.service.ProjetoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProjetoService projetoService;
    @MockBean
    private com.tododev.backend.service.UsuarioService usuarioService;
    @MockBean
    private com.tododev.backend.service.TarefaService tarefaService;
    @BeforeEach
    void setUp() {}

    @Test
    void listarProjetosPorUsuario_deveRetornar200() throws Exception {
        when(projetoService.getProjetosPorUsuario(1L)).thenReturn(List.of(new ProjetoResumoDTO(1L, "Projeto Teste")));
        mockMvc.perform(get("/api/usuarios/1/projetos?usuarioId=1"))
                .andExpect(status().isOk());
    }
}
