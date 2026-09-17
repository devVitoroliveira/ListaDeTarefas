package com.todolist.list.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa os links relacionados a uma tarefa, incluindo operações de busca, atualização, deleção, listagem e criação")
public record TarefaLinksDto(@Schema(description = "Link para ele mesmo") LinkDto self,
        @Schema(description = "Link para atualizar a tarefa") @JsonProperty("update-tarefa") LinkDto updateTarefa,
        @Schema(description = "Link para deletar a tarefa") @JsonProperty("delete-tarefa") LinkDto deleteTarefa,
        @Schema(description = "Link para listar todas as tarefas", example = "{\"href\":\"http://localhost:8080/tarefa\"}") @JsonProperty("all-tarefas") LinkDto allTarefas) {

}
