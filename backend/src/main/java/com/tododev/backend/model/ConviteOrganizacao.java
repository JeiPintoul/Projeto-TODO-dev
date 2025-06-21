package com.tododev.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "convite_organizacao")
public class ConviteOrganizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organizacao_id")
    private Organizacao organizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Funcao funcao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConvite status;

    public enum StatusConvite {
        PENDENTE, ACEITO, RECUSADO
    }
}
