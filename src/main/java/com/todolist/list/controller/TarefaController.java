package com.todolist.list.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
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
import com.todolist.list.service.TarefaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<List<TarefaModel>> createTarefa(@RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
        List<TarefaModel> savedTarefas = tarefaService.saveTarefa(tarefaRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTarefas);

    }

    @GetMapping
    public ResponseEntity<List<TarefaModel>> getAllTarefas() {
        List<TarefaModel> tarefas = tarefaService.getAllTarefas();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TarefaModel>> getTarefaById(@PathVariable UUID id) {
        List<TarefaModel> tarefa = tarefaService.getTarefaById(id);
        if (tarefa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tarefa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<List<TarefaModel>> updateTarefa(@PathVariable UUID id,
            @RequestBody @Valid TarefaRecordDto tarefaRecordDto) {
        List<TarefaModel> updatedTarefa = tarefaService.updateTarefa(id, tarefaRecordDto);
        if (updatedTarefa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTarefa);
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
