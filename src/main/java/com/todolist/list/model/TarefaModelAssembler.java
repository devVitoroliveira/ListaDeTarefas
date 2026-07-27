package com.todolist.list.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.todolist.list.controller.TarefaController;

@Component
public class TarefaModelAssembler implements RepresentationModelAssembler<TarefaModel, EntityModel<TarefaModel>> {

    @Override
    public EntityModel<TarefaModel> toModel(TarefaModel tarefa) {
        return EntityModel.of(tarefa,
                linkTo(methodOn(TarefaController.class).getTarefaById(tarefa.getId())).withSelfRel(),
                linkTo(methodOn(TarefaController.class).updateTarefa(tarefa.getId(), null)).withRel("update-tarefa"),
                linkTo(methodOn(TarefaController.class).deleteTarefa(tarefa.getId())).withRel("delete-tarefa"));
    }

}
