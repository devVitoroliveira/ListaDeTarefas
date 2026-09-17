package com.todolist.list.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representam links para operações relacionadas a uma tarefa")
public record LinkDto(
        @Schema(description = "URL do link", example = "http://localhost:8080/tarefa/3fa85f64-5717-4562-b3fc-2c963f66afa6") String href) {
}
