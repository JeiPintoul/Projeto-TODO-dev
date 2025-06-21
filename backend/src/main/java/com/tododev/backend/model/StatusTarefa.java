package com.tododev.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusTarefa {
    PENDENTE,
    EM_PROGRESSO,
    CONCLUIDO;

    @JsonCreator
    public static StatusTarefa fromString(String value) {
        if (value == null) return null;
        return switch (value.toLowerCase()) {
            case "todo" -> PENDENTE;
            case "progress", "em_progresso", "in_progress" -> EM_PROGRESSO;
            case "done", "concluido", "completed" -> CONCLUIDO;
            default -> throw new IllegalArgumentException("StatusTarefa desconhecido: " + value);
        };
    }
}
