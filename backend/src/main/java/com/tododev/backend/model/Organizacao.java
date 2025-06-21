package com.tododev.backend.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Organizacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode ser vazio")
    @Column(nullable = false, unique = true)
    private String nome;

    @Column
    private String descricao;

    @ManyToMany(mappedBy = "organizacoes")
    private List<Projeto> projetos = new ArrayList<>();

    @OneToMany(mappedBy = "organizacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioOrganizacao> usuariosOrganizacao = new ArrayList<>();
}
