package com.tododev.backend.controller;

import com.tododev.backend.dto.UsuarioRespostaDTO;
import com.tododev.backend.dto.UsuarioDTO;
import com.tododev.backend.model.Usuario;
import com.tododev.backend.service.UsuarioService;
import com.tododev.backend.dto.CriarProjetoDTO;
import com.tododev.backend.model.Projeto;
import com.tododev.backend.dto.ProjetoResumoDTO;
import com.tododev.backend.dto.TarefaRespostaDTO;
import com.tododev.backend.service.ProjetoService;
import com.tododev.backend.service.TarefaService;
import com.tododev.backend.dto.OrganizacaoResumoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final ProjetoService projetoService;
    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<UsuarioRespostaDTO> criarUsuario(@RequestBody @Valid UsuarioDTO dto) {
        Usuario usuario = usuarioService.criarUsuario(
            dto.nome(),
            dto.email(),
            dto.senha(),
            dto.cpf(),
            dto.telefone()
        );
        return ResponseEntity.ok(UsuarioRespostaDTO.fromEntity(usuario, false));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioRespostaDTO>> listarUsuarios() {
        List<UsuarioRespostaDTO> usuarios = usuarioService.listarUsuarios().stream()
            .map(u -> UsuarioRespostaDTO.fromEntity(u, false))
            .toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> getUsuarioPorId(@PathVariable Long id, @RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioService.getUsuarioPorId(id);
            return ResponseEntity.ok(UsuarioRespostaDTO.fromEntity(usuario, false));
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
    public ResponseEntity<List<UsuarioRespostaDTO>> buscarUsuarios(@RequestParam String termo, @RequestParam Long usuarioId) {
        List<UsuarioRespostaDTO> usuarios = usuarioService.buscarUsuarios(termo).stream()
            .map(u -> UsuarioRespostaDTO.fromEntity(u, false))
            .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> atualizarUsuario(@PathVariable Long id, @RequestParam Long usuarioId, @RequestBody @Valid UsuarioDTO dto) {
        // Regra: só o próprio usuário pode atualizar seus dados
        if (!id.equals(usuarioId)) {
            return ResponseEntity.status(403).build();
        }
        Usuario usuario = usuarioService.atualizarUsuario(id, dto.nome(), dto.email(), dto.senha());
        return ResponseEntity.ok(UsuarioRespostaDTO.fromEntity(usuario, false));
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

    @GetMapping("/{id}/organizacoes")
    public ResponseEntity<List<OrganizacaoResumoDTO>> listarOrganizacoesPorUsuario(@PathVariable Long id, @RequestParam Long usuarioId) {
        // Regra: só o próprio usuário pode ver suas organizações
        if (!id.equals(usuarioId)) {
            return ResponseEntity.status(403).build();
        }
        List<OrganizacaoResumoDTO> orgs = usuarioService.listarOrganizacoesPorUsuario(id);
        return ResponseEntity.ok(orgs);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam Long usuarioId) {
        // Simula autenticação: retorna 200 se usuário existe
        usuarioService.getUsuarioPorId(usuarioId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login-simples")
    public ResponseEntity<UsuarioRespostaDTO> loginSimples(
    @RequestParam String email,
    @RequestParam String senha) {
    
    Usuario usuario = usuarioService.buscarPorEmailESenha(email, senha);
    
    if (usuario == null) {
        return ResponseEntity.status(404).build(); // Usuário não encontrado
    }
    
    return ResponseEntity.ok(UsuarioRespostaDTO.fromEntity(usuario, false));
}
}
