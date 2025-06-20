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
    public ResponseEntity<RespostaUsuarioDTO> getUsuarioPorId(@PathVariable Long id, @RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioService.getUsuarioPorId(id);
            return ResponseEntity.ok(RespostaUsuarioDTO.daEntidade(usuario));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/projetos")
    public ResponseEntity<List<ProjetoResumoDTO>> listarProjetosPorUsuario(@PathVariable Long id, @RequestParam Long usuarioId) {
        List<ProjetoResumoDTO> projetos = projetoService.getProjetosPorUsuario(id);
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{id}/tarefas")
    public ResponseEntity<List<TarefaRespostaDTO>> listarTarefasPorUsuario(@PathVariable Long id, @RequestParam Long usuarioId) {
        List<TarefaRespostaDTO> tarefas = tarefaService.getTarefasPorUsuario(id);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RespostaUsuarioDTO>> buscarUsuarios(@RequestParam String termo, @RequestParam Long usuarioId) {
        List<RespostaUsuarioDTO> usuarios = usuarioService.buscarUsuarios(termo).stream()
            .map(RespostaUsuarioDTO::daEntidade)
            .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespostaUsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestParam Long usuarioId, @RequestBody @Valid UsuarioDTO dto) {
        // Regra: só o próprio usuário pode atualizar seus dados
        if (!id.equals(usuarioId)) {
            return ResponseEntity.status(403).build();
        }
        Usuario usuario = usuarioService.atualizarUsuario(id, dto.nome(), dto.email(), dto.senha(), dto.apelido());
        return ResponseEntity.ok(RespostaUsuarioDTO.daEntidade(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id, @RequestParam Long usuarioId) {
        // Regra: só o próprio usuário pode deletar sua conta
        if (!id.equals(usuarioId)) {
            return ResponseEntity.status(403).build();
        }
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{usuarioId}/projetos-pessoais")
    public ResponseEntity<ProjetoResumoDTO> criarProjetoPessoal(@PathVariable Long usuarioId, @RequestParam Long usuarioIdLogado, @RequestBody @Valid CriarProjetoDTO dto) {
        // Regra: só o próprio usuário pode criar projeto pessoal
        if (!usuarioId.equals(usuarioIdLogado)) {
            return ResponseEntity.status(403).build();
        }
        Projeto projeto = projetoService.criarProjetoPessoal(usuarioId, dto.nome(), dto.descricao());
        return ResponseEntity.ok(new ProjetoResumoDTO(projeto.getId(), projeto.getNome()));
    }

    @GetMapping("/{usuarioId}/projetos-pessoais")
    public ResponseEntity<List<ProjetoResumoDTO>> listarProjetosPessoais(@PathVariable Long usuarioId, @RequestParam Long usuarioIdLogado) {
        // Regra: só o próprio usuário pode ver seus projetos pessoais
        if (!usuarioId.equals(usuarioIdLogado)) {
            return ResponseEntity.status(403).build();
        }
        List<ProjetoResumoDTO> projetos = projetoService.listarProjetosPessoais(usuarioId);
        return ResponseEntity.ok(projetos);
    }
}
