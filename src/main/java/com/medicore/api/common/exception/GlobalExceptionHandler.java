package com.medicore.api.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final Clock clock;
    public GlobalExceptionHandler(Clock clock) { this.clock = clock; }
    @ExceptionHandler(ApiException.class)
    ResponseEntity<ProblemDetail> api(ApiException e, HttpServletRequest request) {
        return ResponseEntity.status(e.status()).body(problem(e.status(), e.code(), e.getMessage(), request.getRequestURI(), null));
    }
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    ResponseEntity<ProblemDetail> invalid(Exception e, HttpServletRequest request) {
        List<FieldIssue> errors = e instanceof MethodArgumentNotValidException invalid
                ? invalid.getBindingResult().getFieldErrors().stream().map(this::fieldIssue).toList() : List.of();
        return ResponseEntity.badRequest().body(problem(HttpStatus.BAD_REQUEST, "VALIDACION_INVALIDA", "La solicitud contiene datos inválidos", request.getRequestURI(), errors));
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> unexpected(Exception e, HttpServletRequest request) {
        log.error("Error no controlado al procesar {}", request.getRequestURI(), e);
        return ResponseEntity.internalServerError().body(problem(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO", "Ocurrió un error interno", request.getRequestURI(), null));
    }
    private FieldIssue fieldIssue(FieldError e) { return new FieldIssue(e.getField(), e.getDefaultMessage()); }
    private ProblemDetail problem(HttpStatus status, String code, String detail, String instance, List<FieldIssue> errors) {
        ProblemDetail result = ProblemDetail.forStatusAndDetail(status, detail);
        result.setProperty("code", code); result.setProperty("timestamp", OffsetDateTime.now(clock));
        if (errors != null && !errors.isEmpty()) result.setProperty("errors", errors);
        result.setInstance(java.net.URI.create(instance));
        return result;
    }
    public record FieldIssue(String field, String message) { }
}
