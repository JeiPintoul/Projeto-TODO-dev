package com.tododev.backend.dto;

public record TarefaRespostaDTO(
    String id,
    String projectId,
    String name,
    String description,
    String status,
    String priority,
    String color,
    String startDate,
    String dueDate,
    String artefacts
) {}
