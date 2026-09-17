package com.todolist.list.controller;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.list.configuration.ApiStandardErrors;
import com.todolist.list.dto.TarefaRecordDto;
import com.todolist.list.dto.TarefaResponseDto;
import com.todolist.list.model.TarefaModel;
import com.todolist.list.model.TarefaModelAssembler;
import com.todolist.list.service.TarefaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarefa")
@Tag(name = "Tarefa", description = "API para gerenciamento de tarefas")
public class TarefaController {

    private final TarefaService tarefaService;
    private final TarefaModelAssembler assembler;

    public TarefaController(TarefaService tarefaService, TarefaModelAssembler tarefaModelAssembler) {
        this.tarefaService = tarefaService;
        this.assembler = tarefaModelAssembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<TarefaModel>> createTarefa(@RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
        List<TarefaModel> savedTarefas = tarefaService.saveTarefa(tarefaRecordDto);
        EntityModel<TarefaModel> entityModel = assembler.toModel(savedTarefas.get(0));
        entityModel.add(linkTo(methodOn(TarefaController.class).getAllTarefas()).withRel("all-tarefas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<TarefaModel>>> getAllTarefas() {
        List<TarefaModel> tarefas = tarefaService.getAllTarefas();
        if (tarefas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CollectionModel<EntityModel<TarefaModel>> collection = assembler.toCollectionModel(tarefas);
        collection.add(linkTo(methodOn(TarefaController.class).getAllTarefas()).withSelfRel());
        collection.add(linkTo(methodOn(TarefaController.class).createTarefa(null)).withRel("create-tarefa"));
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Busca uma tarefa pelo ID", description = "Retorna os detalhes de uma tarefa específica com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Tarefa encontrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TarefaResponseDto.class)))
    @ApiStandardErrors
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TarefaModel>> getTarefaById(@PathVariable UUID id) {
        List<TarefaModel> tarefa = tarefaService.getTarefaById(id);
        if (tarefa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<TarefaModel> entityModel = assembler.toModel(tarefa.get(0));
        entityModel.add(linkTo(methodOn(TarefaController.class).getAllTarefas()).withRel("all-tarefas"));
        return ResponseEntity.ok(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<TarefaModel>> updateTarefa(@PathVariable UUID id,
            @RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
        List<TarefaModel> updatedTarefa = tarefaService.updateTarefa(id, tarefaRecordDto);
        if (updatedTarefa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<TarefaModel> entityModel = assembler.toModel(updatedTarefa.get(0));
        entityModel.add(linkTo(methodOn(TarefaController.class).getAllTarefas()).withRel("all-tarefas"));
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<TarefaModel>> deleteTarefa(@PathVariable UUID id) {
        List<TarefaModel> deletedTarefa = tarefaService.deleteTarefa(id);
        if (deletedTarefa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deletedTarefa);
    }
}
