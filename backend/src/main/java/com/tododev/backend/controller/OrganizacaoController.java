package com.tododev.backend.controller;

import com.tododev.backend.dto.OrganizacaoResumoDTO;
import com.tododev.backend.dto.UsuarioOrganizacaoRespostaDTO;
import com.tododev.backend.dto.UsuarioRespostaDTO;
import com.tododev.backend.dto.ProjetoResumoDTO;
import com.tododev.backend.dto.ConviteOrganizacaoDTO;
import com.tododev.backend.dto.UsuarioOrganizacaoRequestDTO;
import com.tododev.backend.model.Funcao;
import com.tododev.backend.model.Organizacao;
import com.tododev.backend.service.OrganizacaoService;
import com.tododev.backend.service.UsuarioOrganizacaoService;
import com.tododev.backend.service.ConviteOrganizacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/organizacoes")
public class OrganizacaoController {
    private final OrganizacaoService organizacaoService;
    private final UsuarioOrganizacaoService usuarioOrganizacaoService;
    private final ConviteOrganizacaoService conviteOrganizacaoService;

    @PostMapping
    public ResponseEntity<OrganizacaoResumoDTO> criarOrganizacao(@RequestBody @Valid OrganizacaoResumoDTO dto, @RequestParam Long usuarioId) {
        Organizacao org = organizacaoService.criarOrganizacao(dto.nome(), dto.descricao(), usuarioId);
        return ResponseEntity.ok(new OrganizacaoResumoDTO(org.getId(), org.getNome(), org.getDescricao()));
    }

    @GetMapping
    public ResponseEntity<List<OrganizacaoResumoDTO>> listarOrganizacoes(@RequestParam Long usuarioId) {
        // Regra: usuário autenticado pode listar organizações que participa
        List<OrganizacaoResumoDTO> orgs = organizacaoService.listarOrganizacoes(usuarioId).stream()
            .map(org -> new OrganizacaoResumoDTO(org.getId(), org.getNome(), org.getDescricao()))
            .toList();
        return ResponseEntity.ok(orgs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizacaoResumoDTO> getOrganizacaoPorId(@PathVariable Long id, @RequestParam Long usuarioId) {
        // Regra: só membros podem ver detalhes
        Organizacao org = organizacaoService.getOrganizacaoPorId(id, usuarioId);
        return ResponseEntity.ok(new OrganizacaoResumoDTO(org.getId(), org.getNome(), org.getDescricao()));
    }

    @GetMapping("/{organizacaoId}/usuarios")
    public ResponseEntity<List<UsuarioOrganizacaoRespostaDTO>> listarUsuariosOrganizacao(@PathVariable Long organizacaoId, @RequestParam Long usuarioId) {
        // Regra: só membros podem listar usuários
        return ResponseEntity.ok(usuarioOrganizacaoService.listarUsuariosPorOrganizacao(organizacaoId, usuarioId));
    }

    @PostMapping("/{organizacaoId}/usuarios")
    public ResponseEntity<UsuarioOrganizacaoRespostaDTO> adicionarUsuarioOrganizacao(@PathVariable Long organizacaoId, @RequestParam Long usuarioId, @RequestBody @Valid UsuarioOrganizacaoRequestDTO dto) {
        // Regra: só gerente pode adicionar usuário
        return ResponseEntity.ok(usuarioOrganizacaoService.adicionarUsuario(organizacaoId, usuarioId, dto));
    }

    @PutMapping("/{organizacaoId}/usuarios/{usuarioId}/funcao")
    public ResponseEntity<Void> alterarFuncaoUsuario(@PathVariable Long organizacaoId, @PathVariable Long usuarioId, @RequestParam Funcao funcao, @RequestParam Long usuarioIdLogado) {
        // Regra: só gerente pode alterar função
        usuarioOrganizacaoService.alterarFuncaoUsuario(organizacaoId, usuarioId, funcao, usuarioIdLogado);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{organizacaoId}/usuarios/{usuarioId}")
    public ResponseEntity<Void> removerUsuarioOrganizacao(@PathVariable Long organizacaoId, @PathVariable Long usuarioId, @RequestParam Long usuarioIdLogado) {
        // Regra: só gerente pode remover usuário
        usuarioOrganizacaoService.removerUsuario(organizacaoId, usuarioId, usuarioIdLogado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarios/{id}/organizacoes")
    public ResponseEntity<List<OrganizacaoResumoDTO>> listarOrganizacoesPorUsuario(@PathVariable Long id, @RequestParam Long usuarioId) {
        // Regra: só o próprio usuário pode ver suas organizações
        if (!id.equals(usuarioId)) {
            return ResponseEntity.status(403).build();
        }
        List<OrganizacaoResumoDTO> orgs = organizacaoService.listarOrganizacoes(id).stream()
            .map(org -> new OrganizacaoResumoDTO(org.getId(), org.getNome(), org.getDescricao()))
            .toList();
        return ResponseEntity.ok(orgs);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioRespostaDTO>> listarUsuarios(@RequestParam(required = false) String termo) {
        List<UsuarioRespostaDTO> usuarios;
        if (termo != null && !termo.isBlank()) {
            usuarios = usuarioOrganizacaoService.buscarUsuariosPorTermo(termo);
        } else {
            usuarios = usuarioOrganizacaoService.listarTodosUsuarios();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{organizacaoId}/projetos")
    public ResponseEntity<List<ProjetoResumoDTO>> listarProjetosPorOrganizacao(@PathVariable Long organizacaoId, @RequestParam(required = false) String termo) {
        List<ProjetoResumoDTO> projetos;
        if (termo != null && !termo.isBlank()) {
            projetos = organizacaoService.buscarProjetosPorOrganizacaoETermo(organizacaoId, termo);
        } else {
            projetos = organizacaoService.listarProjetosPorOrganizacao(organizacaoId);
        }
        return ResponseEntity.ok(projetos);
    }

    @PostMapping("/{organizacaoId}/convidar")
    public ResponseEntity<Void> convidarUsuario(@PathVariable Long organizacaoId, @RequestParam Long usuarioIdLogado, @RequestParam Long usuarioId, @RequestParam Funcao funcao) {
        // Apenas gerente pode convidar
        usuarioOrganizacaoService.validarGerente(organizacaoId, usuarioIdLogado);
        conviteOrganizacaoService.convidarUsuario(organizacaoId, usuarioId, funcao);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/convites/pendentes")
    public ResponseEntity<List<ConviteOrganizacaoDTO>> listarConvitesPendentes(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(conviteOrganizacaoService.listarConvitesPendentes(usuarioId));
    }

    @PostMapping("/convites/{conviteId}/aceitar")
    public ResponseEntity<Void> aceitarConvite(@PathVariable Long conviteId, @RequestParam Long usuarioId) {
        conviteOrganizacaoService.aceitarConvite(conviteId, usuarioId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/convites/{conviteId}/recusar")
    public ResponseEntity<Void> recusarConvite(@PathVariable Long conviteId, @RequestParam Long usuarioId) {
        conviteOrganizacaoService.recusarConvite(conviteId, usuarioId);
        return ResponseEntity.ok().build();
    }
}
