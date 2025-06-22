package com.tododev.backend.repository;

import com.tododev.backend.model.ConviteOrganizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConviteOrganizacaoRepository extends JpaRepository<ConviteOrganizacao, Long> {
    List<ConviteOrganizacao> findByUsuarioIdAndStatus(Long usuarioId, ConviteOrganizacao.StatusConvite status);
    List<ConviteOrganizacao> findByOrganizacaoIdAndStatus(Long organizacaoId, ConviteOrganizacao.StatusConvite status);
    List<ConviteOrganizacao> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndOrganizacaoIdAndStatus(Long usuarioId, Long organizacaoId, ConviteOrganizacao.StatusConvite status);
}
