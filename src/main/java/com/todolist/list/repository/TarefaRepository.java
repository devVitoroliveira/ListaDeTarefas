package com.todolist.list.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todolist.list.model.TarefaModel;

public interface TarefaRepository extends JpaRepository<TarefaModel, UUID> {

}
