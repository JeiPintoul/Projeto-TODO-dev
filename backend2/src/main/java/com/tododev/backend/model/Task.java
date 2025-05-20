package com.tododev.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título da tarefa não pode ser vazio")
    @Column(nullable = false)
    private String titulo;

    @Column
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private Long timeSpent; // time spent in minutes

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manager_id")
    private Usuario manager;

    @ManyToOne
    @JoinColumn(name = "usuario_em_andamento_id")
    private Usuario usuarioEmAndamento;

    @ManyToOne
    @JoinColumn(name = "usuario_concluido_id")
    private Usuario usuarioConcluido;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskArtifact> artifacts = new ArrayList<>();
}
