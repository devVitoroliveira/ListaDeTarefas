package com.todolist.list.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record TarefaCollectionLinksDto(LinkDto self,
        @JsonProperty("create-tarefa") @Schema(example = "{\"href\": \"http://localhost:8080/tarefa\"}") LinkDto createTarefa) {

}
