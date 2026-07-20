package com.todolist.list.exceptions;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResourceExceptionHandler.class);
    @Value("${api.debug:false}")
    private boolean debugMode;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> MethodArgumentNotValidException(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Erro de validação");
        err.setMessage(e.getBindingResult().getFieldError().getDefaultMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<StandardError> NoHandlerFoundException(NoHandlerFoundException e,
            HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Recurso nao encontrado");
        err.setMessage("A URI " + request.getRequestURI() + " nao foi encontrada");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardError> MethodNotSupportedException(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        err.setError("Metodo nao permitido");
        err.setMessage("Metodo " + request.getMethod() + " nao permitido para a URI " + request.getRequestURI()
                + " metodos permitidos: " + e.getSupportedHttpMethods() +
                " Consulte a documentação(Swagger) para mais detalhes.");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> HttpMessageNotReadableException(HttpMessageNotReadableException e,
            HttpServletRequest request) {
        // Sempre loga (independente do perfil)
        logger.error("JSON inválido na URI: {} - Causa: {}",
                request.getRequestURI(), e.getMostSpecificCause().getMessage(), e);

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setPath(request.getRequestURI());

        if (debugMode) {
            // Modo detalhado: mostra a causa raiz "limpa" para o dev
            Throwable cause = e.getMostSpecificCause();
            String detalhe = cause != null ? cause.getMessage() : "Sem detalhes adicionais.";
            err.setError("Requisição inválida (Debug)");
            err.setMessage("Falha ao processar o JSON. Motivo: " + detalhe);
        } else {
            // Modo produção: mensagem genérica e segura
            err.setError("Requisição inválida");
            err.setMessage("O corpo da requisição está malformado. Verifique a sintaxe JSON e os tipos dos campos.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
            HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setPath(request.getRequestURI());

        String paramName = e.getName();
        String requiredType = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "desconhecido";
        String invalidValue = e.getValue() != null ? e.getValue().toString() : "null";

        String message = String.format(
                "O parâmetro '%s' com valor '%s' não pôde ser convertido para o tipo '%s'. Verifique o formato.",
                paramName, invalidValue, requiredType);

        err.setError("Parâmetro inválido");
        err.setMessage(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
