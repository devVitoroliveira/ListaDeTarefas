package com.todolist.list.validator;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NomeTarefaValidator implements ConstraintValidator<NomeTarefaValido, String> {

    private static final Pattern LETRAS_ESPACOS = Pattern.compile("^[\\p{L} ]+$");
    private static final Pattern LETRA_UNICA = Pattern.compile("^[\\p{L}]$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return true; // @NotBlank tratará nulos/vazios

        // 1. Caracteres proibidos (qualquer coisa que não seja letra ou espaço)
        if (!LETRAS_ESPACOS.matcher(value).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O nome da tarefa deve conter apenas letras (com acentos) e espaços. Números e símbolos e pontuação não são permitidos.")
                    .addConstraintViolation();
            return false;
        }

        if (LETRA_UNICA.matcher(value).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O nome da tarefa deve conter mais de uma letra.")
                    .addConstraintViolation();
            return false;
        }
        if(value.contains("  "))
        {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O nome da tarefa contém espaços em excesso.")
                    .addConstraintViolation();
            return false;
        }
        if(value.startsWith(" ") || value.endsWith(" ")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O nome da tarefa contém espaços no início/final.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
