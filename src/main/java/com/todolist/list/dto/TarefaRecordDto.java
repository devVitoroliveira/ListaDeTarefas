package com.todolist.list.dto;

import com.todolist.list.validator.DescricaoTarefaValido;
import com.todolist.list.validator.NomeTarefaValido;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TarefaRecordDto(@Schema(description = "Nome da tarefa", example = "Comprar pão", requiredMode = Schema.RequiredMode.REQUIRED) @NotNull @NotBlank @NomeTarefaValido String nome,
                @Schema(description = "Descrição obrigatória da tarefa. Não pode ser vazia, conter apenas espaços, iniciar ou terminar com espaços/pontuação inválida, possuir espaços duplicados ou ter apenas uma letra. Permite letras, números, espaços, pontuação básica, hífen e barra. Datas, horários e unidades devem usar formatos válidos, como 10/09/2026, 10h30 ou 2kg.", example = "Comprar pão na padaria", requiredMode = Schema.RequiredMode.REQUIRED) @NotNull @NotBlank @DescricaoTarefaValido String descricao,
                @Schema(description = "Indica se a tarefa foi realizada", example = "false", requiredMode = Schema.RequiredMode.REQUIRED) @NotNull Boolean realizado,
                @Schema(description = "Prioridade da tarefa (1 a 5)", example = "3", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "5") @NotNull @Min(1) @Max(5) Integer prioridade) {

}
