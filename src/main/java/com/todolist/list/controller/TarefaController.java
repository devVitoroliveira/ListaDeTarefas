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

import com.todolist.list.dto.TarefaRecordDto;
import com.todolist.list.model.TarefaModel;
import com.todolist.list.model.TarefaModelAssembler;
import com.todolist.list.service.TarefaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarefa")
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
        CollectionModel<EntityModel<TarefaModel>> collection = assembler.toCollectionModel(tarefas);
        collection.add(linkTo(methodOn(TarefaController.class).getAllTarefas()).withSelfRel());
        collection.add(linkTo(methodOn(TarefaController.class).createTarefa(null)).withRel("create-tarefa"));
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}") // (404,500)
    public ResponseEntity<EntityModel<TarefaModel>> getTarefaById(@PathVariable UUID id) {
        List<TarefaModel> tarefa = tarefaService.getTarefaById(id);
        EntityModel<TarefaModel> entityModel = assembler.toModel(tarefa.get(0));
        entityModel.add(linkTo(methodOn(TarefaController.class).getAllTarefas()).withRel("all-tarefas"));
        return ResponseEntity.ok(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<TarefaModel>> updateTarefa(@PathVariable UUID id,
            @RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
        List<TarefaModel> updatedTarefa = tarefaService.updateTarefa(id, tarefaRecordDto);
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
