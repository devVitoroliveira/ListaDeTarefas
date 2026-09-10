package com.todolist.list.dto;

import com.todolist.list.validator.DescricaoTarefaValido;
import com.todolist.list.validator.NomeTarefaValido;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TarefaRecordDto(@NotNull @NotBlank @NomeTarefaValido String nome,
                @NotNull @NotBlank @DescricaoTarefaValido String descricao,
                @NotNull Boolean realizado,
                @NotNull @Min(1) @Max(5) Integer prioridade) {

}
