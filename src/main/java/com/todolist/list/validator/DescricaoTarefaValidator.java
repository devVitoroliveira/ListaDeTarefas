package com.todolist.list.validator;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DescricaoTarefaValidator implements ConstraintValidator<DescricaoTarefaValido, String> {

    // Caracteres proibidos
    private static final Pattern CARACTERES_PROIBIDOS = Pattern.compile("[^\\p{L}0-9 .,;:h/-]");

    // Formatos de data permitidos como palavra completa
    private static final Pattern DATA_HIFEN = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
    private static final Pattern DATA_BARRA = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");

    // Formato de hora permitido
    private static final Pattern HORA = Pattern.compile("\\d{1,2}[h:]\\d{0,2}");

    // Número seguido de UNIDADES ESPECÍFICAS (lista restrita)
    // Ex.: 2kg, 100g, 5m, 10cm, 3un, 1L
    private static final Pattern NUMERO_UNIDADE = Pattern.compile("\\d+(kg|g|m|cm|un|L)");

    // Mistura letra-dígito
    private static final Pattern MISTURA_LETRA_DIGITO = Pattern.compile("\\p{L}\\d|\\d\\p{L}");

    // Pontuação sem espaço
    private static final Pattern PONTUACAO_SEM_ESPACO = Pattern.compile("[,;:](?=\\p{L})");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        // Verificação 0: caracteres proibidos
        if (CARACTERES_PROIBIDOS.matcher(value).find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "A descrição contém caracteres não permitidos. Use apenas letras, números, espaços, pontuação básica (.,;:), hífen e barra.")
                    .addConstraintViolation();
            return false;
        }

        // Verificação 1: início com número
        if (value.matches("^\\d.*")) {

            String primeiraPalavra = value.split("\\s+")[0];
            if (!isPalavraPermitida(primeiraPalavra)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "A descrição começa com número em formato inválido. Use data (dd-mm-aaaa ou dd/mm/aaaa), hora (10h, 10:30) ou número com unidade (2kg, 5m).")
                        .addConstraintViolation();
                return false;
            }
        }
        // Verificação 1.5: Termino com número
        if (value.matches(".*\\d$")) {
            String ultimaPalavra = value.split("\\s+")[value.split("\\s+").length - 1];
            if (!isPalavraPermitida(ultimaPalavra)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "A descrição termina com número em formato inválido. Use data (dd-mm-aaaa ou dd/mm/aaaa), hora (10h, 10:30) ou número com unidade (2kg, 5m).")
                        .addConstraintViolation();
                return false;
            }
        }

        // Verificação 2: para cada palavra, checar se contém mistura letra-dígito
        // proibida
        String[] palavras = value.split("\\s+");
        for (String palavra : palavras) {
            // Se a palavra inteira é válida (data, hora, unidade), ignoramos a verificação
            // de mistura
            if (isPalavraPermitida(palavra))
                continue;

            // Se a palavra NÃO é um contexto permitido, então ela não pode conter mistura
            // letra-dígito
            if (MISTURA_LETRA_DIGITO.matcher(palavra).find()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "A descrição não pode conter palavras que misturem letras e números (ex.: 'c0mprar').")
                        .addConstraintViolation();
                return false;
            }
        }

        // Verificação 3: pontuação sem espaço
        if (PONTUACAO_SEM_ESPACO.matcher(value).find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "A pontuação (.,;:) deve ser seguida de um espaço.").addConstraintViolation();
            return false;
        }
        // Verificação 3.5: pontuação no início/final
        if (value.startsWith(",") || value.startsWith(";") || value.startsWith(":") ||
                value.endsWith(",") || value.endsWith(";") || value.endsWith(":")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("A descrição começa ou termina com pontuação inválida.")
                    .addConstraintViolation();
            return false;
        }

        // Verificação 4: espaços em excesso
        if (value.contains("  ")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("A descrição contém espaços em excesso.")
                    .addConstraintViolation();
            return false;
        }

        // Verificação 5: espaços no início/final
        if (value.startsWith(" ") || value.endsWith(" ")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("A descrição contém espaços no início/final.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    /** Retorna true se a palavra é um formato explicitamente permitido. */
    private boolean isPalavraPermitida(String palavra) {
        return DATA_HIFEN.matcher(palavra).matches() ||
                DATA_BARRA.matcher(palavra).matches() ||
                HORA.matcher(palavra).matches() ||
                NUMERO_UNIDADE.matcher(palavra).matches();
    }

}
