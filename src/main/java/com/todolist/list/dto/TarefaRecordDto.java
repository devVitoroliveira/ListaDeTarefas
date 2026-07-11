package com.todolist.list.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TarefaRecordDto(@NotNull @NotBlank String nome, @NotNull @NotBlank String descricao,
                @NotNull boolean realizado,
                @NotNull int prioridade) {

}
