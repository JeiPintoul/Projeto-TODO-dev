package com.tododev.backend.exception;

import java.time.LocalDateTime;

public record RespostaErro(
    LocalDateTime horario,
    int status,
    String erro,
    String mensagem,
    String caminho
) {
    
}
