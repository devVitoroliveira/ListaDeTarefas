package com.todolist.list.exceptions;

public class TarefaNaoEncontradaException extends RuntimeException {

    public TarefaNaoEncontradaException() {
    }

    public TarefaNaoEncontradaException(String message) {
        super(message);
    }
}
