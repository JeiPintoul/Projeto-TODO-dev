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
    public ResponseEntity<List<OrganizacaoResumoDTO>> listarOrganizacoes() {
        List<OrganizacaoResumoDTO> orgs = organizacaoService.listarOrganizacoes().stream()
            .map(o -> new OrganizacaoResumoDTO(o.getId(), o.getNome(), o.getDescricao()))
            .toList();
        return ResponseEntity.ok(orgs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizacaoResumoDTO> getOrganizacaoPorId(@PathVariable Long id) {
        Organizacao org = organizacaoService.getOrganizacaoPorId(id);
        return ResponseEntity.ok(new OrganizacaoResumoDTO(org.getId(), org.getNome(), org.getDescricao()));
    }

    @GetMapping("/{organizacaoId}/usuarios")
    public ResponseEntity<List<UsuarioOrganizacaoRespostaDTO>> listarUsuariosOrganizacao(@PathVariable Long organizacaoId) {
        return ResponseEntity.ok(usuarioOrganizacaoService.listarUsuariosPorOrganizacao(organizacaoId));
    }

    @PostMapping("/{organizacaoId}/usuarios")
    public ResponseEntity<UsuarioOrganizacaoRespostaDTO> adicionarUsuarioOrganizacao(@PathVariable Long organizacaoId, @RequestBody @Valid AdicionarUsuarioOrganizacaoDTO dto) {
        return ResponseEntity.ok(usuarioOrganizacaoService.adicionarUsuario(organizacaoId, dto));
    }

    @PutMapping("/{organizacaoId}/usuarios/{usuarioId}/funcao")
    public ResponseEntity<Void> alterarFuncaoUsuario(@PathVariable Long organizacaoId, @PathVariable Long usuarioId, @RequestParam Funcao funcao) {
        usuarioOrganizacaoService.alterarFuncaoUsuario(organizacaoId, usuarioId, funcao);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{organizacaoId}/usuarios/{usuarioId}")
    public ResponseEntity<Void> removerUsuarioOrganizacao(@PathVariable Long organizacaoId, @PathVariable Long usuarioId) {
        usuarioOrganizacaoService.removerUsuario(organizacaoId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
