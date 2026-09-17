package com.todolist.list.dto;

import java.util.Map;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa a resposta de uma tarefa, incluindo seus detalhes e links relacionados")
public record TarefaResponseDto(
        @Schema(description = "ID da tarefa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID id,
        @Schema(description = "Nome da tarefa", example = "Comprar mantimento") String nome,
        @Schema(description = "Descrição da tarefa", example = "Comprar mantimento para a semana") String descricao,
        @Schema(description = "Indica se a tarefa foi realizada", example = "false") Boolean realizado,
        @Schema(description = "Prioridade da tarefa", example = "1") Integer prioridade,
        @Schema(description = "Links relacionados à tarefa") TarefaLinksDto _links) {

}
