package com.todolist.list.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa a resposta de uma coleção de tarefas, incluindo seus detalhes e links relacionados")
public record TarefaCollectionResponseDto(
                @JsonProperty("_embedded") EmbeddedDto _embedded,
                @JsonProperty("_links") TarefaCollectionLinksDto _links) {
        public record EmbeddedDto(
                        List<TarefaResponseDto> tarefaModelList) {
        }
}
