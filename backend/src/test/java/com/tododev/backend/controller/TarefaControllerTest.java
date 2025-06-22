package com.tododev.backend.controller;

import com.tododev.backend.service.TarefaService;
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

@WebMvcTest(TarefaController.class)
class TarefaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TarefaService tarefaService;
    @MockBean
    private com.tododev.backend.service.ProjetoService projetoService;
    @MockBean
    private com.tododev.backend.service.UsuarioService usuarioService;
    @BeforeEach
    void setUp() {}

    @Test
    void listarTarefasPorProjeto_deveRetornar200() throws Exception {
        when(tarefaService.getTarefasPorProjeto(1L)).thenReturn(List.of());
        mockMvc.perform(get("/api/1/1/tarefas?usuarioId=1"))
                .andExpect(status().isOk());
    }
}
