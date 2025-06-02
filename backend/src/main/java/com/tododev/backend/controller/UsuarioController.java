package com.tododev.backend.controller;

import com.tododev.backend.dto.RespostaUsuarioDTO;
import com.tododev.backend.dto.UsuarioDTO;
import com.tododev.backend.model.Usuario;
import com.tododev.backend.service.UsuarioService;
import com.tododev.backend.dto.CriarProjetoDTO;
import com.tododev.backend.model.Projeto;
import com.tododev.backend.dto.ProjetoResumoDTO;
import com.tododev.backend.dto.TarefaRespostaDTO;
import com.tododev.backend.service.ProjetoService;
import com.tododev.backend.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final ProjetoService projetoService;
    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<RespostaUsuarioDTO> criarUsuario(@RequestBody @Valid UsuarioDTO dto) {
        Usuario usuario = usuarioService.criarUsuario(dto.nome(), dto.email(), dto.senha(), dto.apelido());
        return ResponseEntity.ok(RespostaUsuarioDTO.daEntidade(usuario));
    }

    @GetMapping
    public ResponseEntity<List<RespostaUsuarioDTO>> listarUsuarios() {
        List<RespostaUsuarioDTO> usuarios = usuarioService.listarUsuarios().stream()
            .map(RespostaUsuarioDTO::daEntidade)
            .toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaUsuarioDTO> getUsuarioPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.getUsuarioPorId(id);
            return ResponseEntity.ok(RespostaUsuarioDTO.daEntidade(usuario));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/projetos")
    public ResponseEntity<List<ProjetoResumoDTO>> listarProjetosPorUsuario(@PathVariable Long id) {
        List<ProjetoResumoDTO> projetos = projetoService.getProjetosPorUsuario(id);
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{id}/tarefas")
    public ResponseEntity<List<TarefaRespostaDTO>> listarTarefasPorUsuario(@PathVariable Long id) {
        List<TarefaRespostaDTO> tarefas = tarefaService.getTarefasPorUsuario(id);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RespostaUsuarioDTO>> buscarUsuarios(@RequestParam String termo) {
        List<RespostaUsuarioDTO> usuarios = usuarioService.buscarUsuarios(termo).stream()
            .map(RespostaUsuarioDTO::daEntidade)
            .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/{usuarioId}/projetos-pessoais")
    public ResponseEntity<ProjetoResumoDTO> criarProjetoPessoal(@PathVariable Long usuarioId, @RequestBody @Valid CriarProjetoDTO dto) {
        Projeto projeto = projetoService.criarProjetoPessoal(usuarioId, dto.nome(), dto.descricao());
        return ResponseEntity.ok(new ProjetoResumoDTO(projeto.getId(), projeto.getNome()));
    }

    @GetMapping("/{usuarioId}/projetos-pessoais")
    public ResponseEntity<List<ProjetoResumoDTO>> listarProjetosPessoais(@PathVariable Long usuarioId) {
        List<ProjetoResumoDTO> projetos = projetoService.listarProjetosPessoais(usuarioId);
        return ResponseEntity.ok(projetos);
    }
}
