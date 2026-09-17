package com.todolist.list.dto;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa a resposta de uma coleção de tarefas, incluindo seus detalhes e links relacionados")
public record TarefaCollectionResponseDto(
        @Schema(description = "Coleção de tarefas") Map<String, List<TarefaResponseDto>> tarefas,
        @Schema(description = "Links relacionados à coleção de tarefas") LinkDto links) {

}
