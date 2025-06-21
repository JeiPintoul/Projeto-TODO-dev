package com.tododev.backend.controller;

import com.tododev.backend.dto.AdicionarUsuarioOrganizacaoDTO;
import com.tododev.backend.dto.OrganizacaoResumoDTO;
import com.tododev.backend.dto.UsuarioOrganizacaoRespostaDTO;
import com.tododev.backend.model.Funcao;
import com.tododev.backend.model.Organizacao;
import com.tododev.backend.service.OrganizacaoService;
import com.tododev.backend.service.UsuarioOrganizacaoService;
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

    @PostMapping
    public ResponseEntity<OrganizacaoResumoDTO> criarOrganizacao(@RequestBody @Valid OrganizacaoResumoDTO dto) {
        Organizacao org = organizacaoService.criarOrganizacao(dto.nome(), dto.descricao());
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
    public ResponseEntity<UsuarioOrganizacaoRespostaDTO> adicionarUsuarioOrganizacao(@PathVariable Long organizacaoId, @RequestParam Long usuarioId, @RequestBody @Valid AdicionarUsuarioOrganizacaoDTO dto) {
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
}
