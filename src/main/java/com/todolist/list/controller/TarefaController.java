package com.todolist.list.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        var tarefaModel = new TarefaModel();
        BeanUtils.copyProperties(tarefaRecordDto, tarefaModel);
        List<TarefaModel> savedTarefas = tarefaService.saveTarefa(tarefaModel);
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
}
