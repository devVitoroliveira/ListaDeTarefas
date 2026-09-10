package com.todolist.list.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.todolist.list.dto.TarefaRecordDto;
import com.todolist.list.model.TarefaModel;
import com.todolist.list.repository.TarefaRepository;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public List<TarefaModel> saveTarefa(TarefaRecordDto tarefa) {
        var tarefaModel = new TarefaModel();
        BeanUtils.copyProperties(tarefa, tarefaModel);
        tarefaRepository.save(tarefaModel);
        return List.of(tarefaModel);
    }

    public List<TarefaModel> getAllTarefas() {
        Sort s = Sort.by(Sort.Direction.ASC, "id");
        return tarefaRepository.findAll(s);
    }

    public List<TarefaModel> getTarefaById(UUID id) {
        Optional<TarefaModel> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            TarefaModel tarefa = tarefaOptional.get();
            return List.of(tarefa);
        } else {
            return List.of();
        }
    }

    public List<TarefaModel> updateTarefa(UUID id, TarefaRecordDto tarefa) {
        Optional<TarefaModel> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            TarefaModel tarefaToUpdate = tarefaOptional.get();
            BeanUtils.copyProperties(tarefa, tarefaToUpdate);
            tarefaRepository.save(tarefaToUpdate);
            return getAllTarefas();
        } else {
            return List.of();
        }
    }

    public List<TarefaModel> deleteTarefa(UUID id) {
        Optional<TarefaModel> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            TarefaModel tarefaToDelete = tarefaOptional.get();
            tarefaRepository.delete(tarefaToDelete);
            return getAllTarefas();
        } else {
            return List.of();
        }
    }
}
