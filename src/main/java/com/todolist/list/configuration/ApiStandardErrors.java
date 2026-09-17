package com.todolist.list.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.todolist.list.exceptions.StandardError;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
                @ApiResponse(responseCode = "400", description = "Requisição inválida. Pode ocorrer devido a dados inválidos no corpo da requisição, ou parâmetros inválidos na URL", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                @ApiResponse(responseCode = "404", description = "Tarefa não encontrada ou recurso não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                @ApiResponse(responseCode = "405", description = "Método HTTP não permitido para o recurso solicitado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
})
public @interface ApiStandardErrors {

}
