package com.tododev.backend.repository;

import com.tododev.backend.model.UsuarioOrganizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioOrganizacaoRepository extends JpaRepository<UsuarioOrganizacao, Long> {
    List<UsuarioOrganizacao> findByOrganizacaoId(Long organizacaoId);
    List<UsuarioOrganizacao> findByUsuarioId(Long usuarioId);
    UsuarioOrganizacao findByUsuarioIdAndOrganizacaoId(Long usuarioId, Long organizacaoId);
}
