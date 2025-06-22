package com.tododev.backend.dto;

public record TarefaRequestDTO(
    String name,
    String description,
    String status,
    String priority,
    String artefacts
) {}
