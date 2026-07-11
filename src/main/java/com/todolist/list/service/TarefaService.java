package com.todolist.list.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.todolist.list.model.TarefaModel;
import com.todolist.list.repository.TarefaRepository;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public List<TarefaModel> saveTarefa(TarefaModel tarefa) {
        var tarefaModel = new TarefaModel();
        BeanUtils.copyProperties(tarefa, tarefaModel);
        tarefaRepository.save(tarefaModel);
        return tarefaRepository.findAll();
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
}
